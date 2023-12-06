package com.hmdp.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

@Service
public class NioServerService {

    @Async
    public void startServer() {
        ///需要先访问startServer启动服务
        try {
            // 创建一个 Selector
            Selector selector = Selector.open();

            // 创建一个 ServerSocketChannel，并绑定到指定端口
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(8080));
            serverSocketChannel.configureBlocking(false);

            // 将 ServerSocketChannel 注册到 Selector，并关注 OP_ACCEPT 事件
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                // 阻塞等待事件发生
                int readyChannels = selector.select();

                if (readyChannels > 0) {
                    // 获取已就绪的事件集合
                    Set<SelectionKey> selectedKeys = selector.selectedKeys();
                    Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                    while (keyIterator.hasNext()) {
                        SelectionKey key = keyIterator.next();

                        if (key.isAcceptable()) {
                            // 处理 OP_ACCEPT 事件
                            handleAccept(key, selector);
                        } else if (key.isReadable()) {
                            // 处理 OP_READ 事件
                            handleRead(key);
                        }

                        keyIterator.remove();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleAccept(SelectionKey key, Selector selector) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        int bytesRead = socketChannel.read(buffer);

        if (bytesRead == -1) {
            // 客户端关闭连接
            socketChannel.close();
        } else if (bytesRead > 0) {
            // 处理读取到的数据
            buffer.flip();
            byte[] data = new byte[buffer.limit()];
            buffer.get(data);
            // 将读取到的数据原封不动地写回客户端
            socketChannel.write(ByteBuffer.wrap(data));
            System.out.println("Received: " + new String(data));
        }
    }
}

