package com.zjut.grade.manager.chat;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.DataListener;
import com.zjut.grade.manager.chatObject.ChatObject;

import java.util.Set;

/**
 * User:huangtao
 * Date:2016-02-23
 * description：
 */
public class RoomLauncher {
    private SocketIOServer ioServer;

    public void start() {
        Configuration configuration = new Configuration();
        configuration.setHostname("localhost");
        configuration.setPort(9093);
        ioServer = new SocketIOServer(configuration);
        addOrdersEvent();
        addRoomsEvent();
        ioServer.start();
    }

    public void stop() throws InterruptedException {
        Thread.sleep(Integer.MAX_VALUE);
        ioServer.stop();
    }

    public void addRoomsEvent() {
        final SocketIONamespace cartNameSpace = ioServer.addNamespace("/cart");
        cartNameSpace.addEventListener("joinCart", ChatObject.class, new DataListener<ChatObject>() {
            @Override
            public void onData(final SocketIOClient client, ChatObject data, AckRequest ackSender) throws Exception {
                client.joinRoom("room");
                SocketIONamespace clientSpace = client.getNamespace();
                System.out.println(clientSpace.getName());
                ChatObject o = new ChatObject();
                o.setUserName("joinCart");
                client.sendEvent("joinCartClient", new AckCallback<String>(String.class) {
                    @Override
                    public void onSuccess(String result) {
                        System.out.println("成功向客户端发送消息！" + client.getSessionId());
                        System.out.println(result);
                    }
                }, o);
                cartNameSpace.getRoomOperations("room").sendEvent("joinCart", data);
            }
        });

        cartNameSpace.addEventListener("changeCart", ChatObject.class, new DataListener<ChatObject>() {
            @Override
            public void onData(SocketIOClient client, ChatObject data, AckRequest ackSender) throws Exception {
                client.sendEvent("changeCartClient", new AckCallback<ChatObject>(ChatObject.class) {
                    @Override
                    public void onSuccess(ChatObject result) {
                        System.out.println("result2:   " + result);
                    }
                });
                cartNameSpace.getRoomOperations("room2").sendEvent("changeCart", data);
            }
        });
    }

    public void addOrdersEvent() {
        final SocketIONamespace ordersNameSpace = ioServer.addNamespace("/order");
        ordersNameSpace.addEventListener("confirmOrder", ChatObject.class, new DataListener<ChatObject>() {
            @Override
            public void onData(SocketIOClient client, ChatObject data, AckRequest ackSender) throws Exception {
                Set rooms = client.getAllRooms();
                rooms.isEmpty();
                ordersNameSpace.getBroadcastOperations().sendEvent("confirmOrder", data);
            }
        });
    }

    public static void main(String[] args) throws InterruptedException {
        RoomLauncher roomLauncher = new RoomLauncher();
        roomLauncher.start();
        roomLauncher.stop();
    }
}
