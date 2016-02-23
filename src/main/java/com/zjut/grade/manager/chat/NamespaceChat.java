package com.zjut.grade.manager.chat;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.DataListener;
import com.zjut.grade.manager.chatObject.ChatObject;
import com.zjut.grade.manager.chatObject.Room;

/**
 * User:huangtao
 * Date:2016-02-15
 * description：
 */
public class NamespaceChat {
    protected static void join(SocketIOServer ioServer, String nameSpace, final ChatObject chatObject, final Room room) {
        final SocketIONamespace chatNamespace = ioServer.addNamespace(nameSpace);

        chatNamespace.addEventListener("join", ChatObject.class, new DataListener<ChatObject>() {
            @Override
            public void onData(SocketIOClient client, ChatObject data, AckRequest ackSender) throws Exception {
                chatNamespace.getRoomOperations(room.getRoomName()).sendEvent("join", chatObject);
            }
        });
    }

    protected static void start(SocketIOServer ioServer) {
        ioServer.start();
    }

    public static void main(String[] Args) {
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(9091);


        final SocketIOServer ioServer = new SocketIOServer(config);
        ioServer.addNamespace("join");
        final SocketIONamespace chatNamespace = ioServer.getNamespace("join");

        chatNamespace.addEventListener("join", ChatObject.class, new DataListener<ChatObject>() {
            @Override
            public void onData(SocketIOClient client, ChatObject data, AckRequest ackSender) throws Exception {
                chatNamespace.getRoomOperations(data.getRoomName()).sendEvent("join", data);
            }
        });

//        ioServer.addEventListener("join", ChatObject.class, new DataListener<ChatObject>() {
//            @Override
//            public void onData(SocketIOClient client, ChatObject data, AckRequest ackSender) throws Exception {
//                ioServer.getBroadcastOperations().sendEvent("join",data);
//            }
//        });


//        ChatObject chatObject = new ChatObject();
//        chatObject.setName("11");
//        chatObject.setMessage("11join");
//        Room room = new Room();
//        room.setRoomName("edu");
//        String nameSpace1 = "zjut";
//        join(ioServer, nameSpace1, chatObject, room);
//        ChatObject chatObject2 = new ChatObject();
//        chatObject2.setName("22");
//        chatObject2.setMessage("22join");
//        Room room2 = new Room();
//        room2.setRoomName("zjut");
//        join(ioServer, nameSpace1, chatObject2, room2);
        start(ioServer);
    }
}
