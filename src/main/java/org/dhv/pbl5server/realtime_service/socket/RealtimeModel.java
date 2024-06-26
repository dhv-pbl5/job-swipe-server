package org.dhv.pbl5server.realtime_service.socket;

import lombok.*;
import org.dhv.pbl5server.common_service.constant.CommonConstant;
import org.dhv.pbl5server.common_service.utils.CommonUtils;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealtimeModel {
    private Object type;
    private Object data;

    public static String toJson(Object type, Object data) {
        var obj = new RealtimeModel(type, data);
        return "%s%s".formatted(CommonUtils.convertToJson(obj), CommonConstant.NOTIFICATION_DATA_END_SYMBOL);
    }
}
