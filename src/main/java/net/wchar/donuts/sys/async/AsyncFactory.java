package net.wchar.donuts.sys.async;

import net.wchar.donuts.model.po.SysOpLogPo;
import net.wchar.donuts.service.SysOpLogService;
import net.wchar.donuts.sys.util.BeanUtils;

import java.util.TimerTask;

/**
 * 异步工厂
 * @author Elijah
 */
public class AsyncFactory
{
    /**
     * 操作日志记录
     *
     * @param opLog 操作日志信息
     * @return 任务task
     */
    public static TimerTask recordOp(final SysOpLogPo opLog)
    {
        return new TimerTask()
        {
            @Override
            public void run()
            {
                BeanUtils.getBean(SysOpLogService.class).save(opLog);
            }
        };
    }
}