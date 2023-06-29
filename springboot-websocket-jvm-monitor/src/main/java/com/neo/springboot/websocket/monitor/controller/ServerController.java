package com.neo.springboot.websocket.monitor.controller;

import cn.hutool.core.lang.Dict;
import com.neo.springboot.websocket.monitor.bean.Server;
import com.neo.springboot.websocket.monitor.payload.ServerVO;
import com.neo.springboot.websocket.monitor.utils.ServerUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/server")
public class ServerController {

    @GetMapping
    public Dict serverInfo() throws Exception {
        Server server = new Server();
        server.copyTo();
        ServerVO serverVO = ServerUtil.wrapServerVO(server);
        return ServerUtil.wrapServerDict(serverVO);
    }

}