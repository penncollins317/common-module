package top.echovoid.dns.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;

public class DnsServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
        ByteBuf buf = packet.content();

        // 读取 transaction ID（前2字节）
        int transactionId = buf.readUnsignedShort();

        // 读取 Flags
        int flags = buf.readUnsignedShort();

        // 问题数量
        int questionCount = buf.readUnsignedShort();

        // 简化示例：只处理1个问题
        buf.skipBytes(6); // 跳过 Answer, Authority, Additional Counts

        // 读取域名
        StringBuilder domain = new StringBuilder();
        int len;
        while ((len = buf.readByte()) > 0) {
            byte[] label = new byte[len];
            buf.readBytes(label);
            domain.append(new String(label)).append(".");
        }
        domain.setLength(domain.length() - 1); // 去掉最后一个.

        int qtype = buf.readUnsignedShort();
        int qclass = buf.readUnsignedShort();

        System.out.println("请求解析: " + domain);

        // 构造响应
        ByteBuf outBuf = Unpooled.buffer();
        outBuf.writeShort(transactionId);
        outBuf.writeShort(0x8180); // 标志位: 标准响应 + 可用
        outBuf.writeShort(1); // Question Count
        outBuf.writeShort(1); // Answer Count
        outBuf.writeShort(0); // Authority Count
        outBuf.writeShort(0); // Additional Count

        // Question Section
        writeDomainName(outBuf, domain.toString());
        outBuf.writeShort(qtype); // QTYPE
        outBuf.writeShort(qclass); // QCLASS

        // Answer Section
        writeDomainName(outBuf, domain.toString());
        outBuf.writeShort(1); // TYPE A
        outBuf.writeShort(1); // CLASS IN
        outBuf.writeInt(60); // TTL
        outBuf.writeShort(4); // RDLENGTH
        outBuf.writeBytes(new byte[]{118, 24, 41, 39}); // IP Address

        ctx.writeAndFlush(new DatagramPacket(outBuf, packet.sender()));
    }

    private void writeDomainName(ByteBuf buf, String domain) {
        for (String label : domain.split("\\.")) {
            buf.writeByte(label.length());
            buf.writeBytes(label.getBytes());
        }
        buf.writeByte(0); // end
    }
}
