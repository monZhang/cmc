package com.cm.controller;

import com.cm.common.JsonResult;
import com.cm.entity.Inventory;
import com.cm.inventory.request.RefreshInventory2Cache;
import com.cm.inventory.request.UpdateInventory2DB;
import com.cm.inventory.service.InventoryService;
import com.cm.inventory.service.RequestAsyncProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 库存更新,获取
 */
@Slf4j
@RestController
@RequestMapping("/inventory")
public class InventoryController {

    @Resource
    private RequestAsyncProcessService asyncProcessService;
    @Resource
    private InventoryService inventoryService;

    @GetMapping("/{productId:\\d+}")
    public JsonResult getInventory(@PathVariable Long productId) {
        try {
            //读缓存,获取成功直接返回
            Inventory inventory = inventoryService.getInventoryFromCache(productId);
            if (Objects.nonNull(inventory)) {
                log.debug("同步读取库存缓存成功, inventory: {} ,直接返回.", inventory);
                return JsonResult.success(inventory);
            }
            //redis直接获取失败,加入队列处理,异步获取db数据并加入缓存
            asyncProcessService.process(new RefreshInventory2Cache(productId, inventoryService, false));
            long startTime = System.currentTimeMillis();
            while (true) {
                //等待异步任务处理,超过200ms,从缓存获取失败跳出循环.
                if (System.currentTimeMillis() - startTime > 200) {
                    log.warn("等待异步任务处理完成读取库存缓存, 等待时间超过200ms, 从库存缓存获取信息失败, 降级为直接从db获取.");
                    break;
                }
                inventory = inventoryService.getInventoryFromCache(productId);
                if (Objects.nonNull(inventory)) {
                    return JsonResult.success(inventory);
                }
                Thread.sleep(10);
            }
            //从缓存获取库存失败, 查询数据库并返回
            Inventory inventoryDB = inventoryService.getInventoryById(productId);
            if (Objects.nonNull(inventoryDB)) {
                //加入缓存,应对后续请求.
                inventoryService.put2Cache(inventoryDB);
                //提交一个强制刷新请求到队列,防止上一步造成数据不一致情况.
                asyncProcessService.process(new RefreshInventory2Cache(productId, inventoryService, true));
            }
            return JsonResult.success(inventoryDB);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail("获取库存信息失败!");
        }
    }

    @PutMapping
    public JsonResult updateInventory(Inventory inventory) {
        try {
            //所有的写请求都需要加入队列处理,不进行去重,所有forceRefresh = true
            asyncProcessService.process(new UpdateInventory2DB(inventory, inventoryService));
            return JsonResult.success();
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.fail("更新库存信息失败!");
        }
    }
}
