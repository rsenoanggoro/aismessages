/*
 * AISMessages
 * - a java-based library for decoding of AIS messages from digital VHF radio traffic related
 * to maritime navigation and safety in compliance with ITU 1371.
 * 
 * (C) Copyright 2011-2013 by S-Consult ApS, DK31327490, http://s-consult.dk, Denmark.
 * 
 * Released under the Creative Commons Attribution-NonCommercial-ShareAlike 3.0 Unported License.
 * For details of this license see the nearby LICENCE-full file, visit http://creativecommons.org/licenses/by-nc-sa/3.0/
 * or send a letter to Creative Commons, 171 Second Street, Suite 300, San Francisco, California, 94105, USA.
 * 
 * NOT FOR COMMERCIAL USE!
 * Contact sales@s-consult.dk to obtain a commercially licensed version of this software.
 * 
 */

package dk.tbsalling.aismessages.messages;

import dk.tbsalling.aismessages.decoder.DecoderImpl;
import dk.tbsalling.aismessages.exceptions.InvalidEncodedMessage;
import dk.tbsalling.aismessages.exceptions.UnsupportedMessageType;
import dk.tbsalling.aismessages.messages.types.AISMessageType;
import dk.tbsalling.aismessages.messages.types.IMO;
import dk.tbsalling.aismessages.messages.types.MMSI;
import dk.tbsalling.aismessages.messages.types.PositionFixingDevice;
import dk.tbsalling.aismessages.messages.types.ShipType;

/**
 * Message has a total of 424 bits, occupying two AIVDM sentences. In practice,
 * the information in these fields (especially ETA and destination) is not
 * reliable, as it has to be hand-updated by humans rather than gathered
 * automatically from sensors.
 * 
 * @author tbsalling
 */
@SuppressWarnings("serial")
public class ShipAndVoyageData extends DecodedAISMessage {
	
	private ShipAndVoyageData(Integer repeatIndicator,
			MMSI mmsi, IMO imo, String callsign, String shipName,
			ShipType shipType, Integer toBow, Integer toStern,
			Integer toStarboard, Integer toPort,
			PositionFixingDevice positionFixingDevice, String eta, Float draught,
			String destination, Boolean dataTerminalReady) {
		super(AISMessageType.ShipAndVoyageRelatedData, repeatIndicator, mmsi);
		this.imo = imo;
		this.callsign = callsign;
		this.shipName = shipName;
		this.shipType = shipType;
		this.toBow = toBow;
		this.toStern = toStern;
		this.toStarboard = toStarboard;
		this.toPort = toPort;
		this.positionFixingDevice = positionFixingDevice;
		this.eta = eta;
		this.draught = draught;
		this.destination = destination;
		this.dataTerminalReady = dataTerminalReady;
	}

	public final IMO getImo() {
		return imo;
	}

	public final String getCallsign() {
		return callsign;
	}

	public final String getShipName() {
		return shipName;
	}

	public final ShipType getShipType() {
		return shipType;
	}

	public final Integer getToBow() {
		return toBow;
	}

	public final Integer getToStern() {
		return toStern;
	}

	public final Integer getToStarboard() {
		return toStarboard;
	}

	public final Integer getToPort() {
		return toPort;
	}

	public final PositionFixingDevice getPositionFixingDevice() {
		return positionFixingDevice;
	}

	public final String getEta() {
		return eta;
	}

	public final Float getDraught() {
		return draught;
	}

	public final String getDestination() {
		return destination;
	}

	public final Boolean getDataTerminalReady() {
		return dataTerminalReady;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ShipAndVoyageData [imo=").append(imo)
				.append(", callsign=").append(callsign).append(", shipName=")
				.append(shipName).append(", shipType=").append(shipType)
				.append(", toBow=").append(toBow).append(", toStern=")
				.append(toStern).append(", toStarboard=").append(toStarboard)
				.append(", toPort=").append(toPort)
				.append(", positionFixingDevice=").append(positionFixingDevice)
				.append(", eta=").append(eta).append(", draught=")
				.append(draught).append(", destination=").append(destination)
				.append(", dataTerminalReady=").append(dataTerminalReady)
				.append("]");
		return builder.toString();
	}

	public static ShipAndVoyageData fromEncodedMessage(EncodedAISMessage encodedMessage) {
		if (! encodedMessage.isValid())
			throw new InvalidEncodedMessage(encodedMessage);
		if (! encodedMessage.getMessageType().equals(AISMessageType.ShipAndVoyageRelatedData))
			throw new UnsupportedMessageType(encodedMessage.getMessageType().getCode());
			
		Integer repeatIndicator = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(6, 8));
		MMSI sourceMmsi = MMSI.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(8, 38)));
		IMO imo = IMO.valueOf(DecoderImpl.convertToUnsignedLong(encodedMessage.getBits(40, 70)));
		String callsign = DecoderImpl.convertToString(encodedMessage.getBits(70, 112));
		String shipName = DecoderImpl.convertToString(encodedMessage.getBits(112, 232));
		ShipType shipType = ShipType.fromInteger(DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(232, 240)));
		Integer toBow = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(240, 249));
		Integer toStern = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(249, 258));
		Integer toPort = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(258, 264));
		Integer toStarboard = DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(264, 270));
		PositionFixingDevice positionFixingDevice = PositionFixingDevice.fromInteger(DecoderImpl.convertToUnsignedInteger(encodedMessage.getBits(270, 274)));
		String eta = DecoderImpl.convertToTime(encodedMessage.getBits(274, 294));
		Float draught = DecoderImpl.convertToUnsignedFloat(encodedMessage.getBits(294, 302)) / 10f;
		String destination = DecoderImpl.convertToString(encodedMessage.getBits(302, 422));
		Boolean dataTerminalReady = DecoderImpl.convertToBoolean(encodedMessage.getBits(422, 423));
		
		return new ShipAndVoyageData(repeatIndicator, sourceMmsi, imo, callsign, shipName, shipType, toBow, toStern, toStarboard, toPort, positionFixingDevice, eta, draught, destination, dataTerminalReady);
	}
		
	private final IMO imo;
	private final String callsign;
	private final String shipName;
	private final ShipType shipType;
	private final Integer toBow;
	private final Integer toStern;
	private final Integer toStarboard;
	private final Integer toPort;
	private final PositionFixingDevice positionFixingDevice;
	private final String eta;
	private final Float draught;
	private final String destination;
	private final Boolean dataTerminalReady;
}
