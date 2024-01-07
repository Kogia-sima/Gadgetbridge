/*  Copyright (C) 2021-2023 Gaignon Damien
    Copyright (C) 2022-2023 MartinJM

    This file is part of Gadgetbridge.

    Gadgetbridge is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published
    by the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Gadgetbridge is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>. */
package nodomain.freeyourgadget.gadgetbridge.service.devices.huawei.requests;

import static nodomain.freeyourgadget.gadgetbridge.devices.huawei.packets.DeviceConfig.LinkParams;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import nodomain.freeyourgadget.gadgetbridge.devices.huawei.HuaweiPacket;
import nodomain.freeyourgadget.gadgetbridge.devices.huawei.packets.DeviceConfig;
import nodomain.freeyourgadget.gadgetbridge.service.devices.huawei.HuaweiSupportProvider;

// GetLinkParamsRequest<HuaweiBtLESupport, BtLERequest>
public class GetLinkParamsRequest extends Request {
    private static final Logger LOG = LoggerFactory.getLogger(GetLinkParamsRequest.class);

    public byte[] serverNonce;
    public byte bondState;

    public GetLinkParamsRequest(
            HuaweiSupportProvider support,
            nodomain.freeyourgadget.gadgetbridge.service.btbr.TransactionBuilder builder
    ) {
        super(support, builder);
        this.serviceId = DeviceConfig.id;
        this.commandId = LinkParams.id;
        this.serverNonce = new byte[18];
        isSelfQueue = false;
    }

    public GetLinkParamsRequest(
            HuaweiSupportProvider support,
            nodomain.freeyourgadget.gadgetbridge.service.btle.TransactionBuilder builder
    ) {
        super(support, builder);
        this.serviceId = DeviceConfig.id;
        this.commandId = LinkParams.id;
        this.serverNonce = new byte[18];
        isSelfQueue = false;
    }

    @Override
    protected List<byte[]> createRequest() throws RequestCreationException {
        try {
            return new LinkParams.Request(paramsProvider, supportProvider.getHuaweiType()).serialize();
        } catch (HuaweiPacket.CryptoException e) {
            throw new RequestCreationException(e);
        }
    }

    @Override
    protected void processResponse() throws ResponseParseException {
        LOG.debug("handle LinkParams");

        if (!(receivedPacket instanceof LinkParams.Response))
            throw new ResponseTypeMismatchException(receivedPacket, LinkParams.Response.class);

        supportProvider.setProtocolVersion(((LinkParams.Response) receivedPacket).protocolVersion);
        paramsProvider.setAuthMode(((LinkParams.Response) receivedPacket).authMode);

        paramsProvider.setSliceSize(((LinkParams.Response) receivedPacket).sliceSize);
        paramsProvider.setMtu(((LinkParams.Response) receivedPacket).mtu);

        this.serverNonce = ((LinkParams.Response) receivedPacket).serverNonce;
        paramsProvider.setAuthVersion(((LinkParams.Response) receivedPacket).authVersion);

        this.bondState = ((LinkParams.Response) receivedPacket).bondState;
    }
}
