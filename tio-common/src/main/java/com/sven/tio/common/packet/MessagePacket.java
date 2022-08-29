package com.sven.tio.common.packet;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.tio.core.intf.Packet;

/**
 * @author qinglinl
 * Created on 2022/8/24 1:40 PM
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MessagePacket extends Packet {
	private static final long serialVersionUID = 899113436159984396L;

	public static final int HEADER_LENGTH = 4;

	public static final String CHARSET = "utf-8";

	private byte[] body;
}
