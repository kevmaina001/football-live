package com.tvfootballhd.liveandstream.Utils;

import com.google.android.exoplayer2.extractor.ts.TsExtractor;

public class Config {
    private static String apiKey = "3bdb371035msh4a88eff8edf2ec5p103690jsnd48d0c24e31b";
    public static int[] leagueId = {61, 2, 3, 848, 5, 39, 45, 40, 140, 78, TsExtractor.TS_STREAM_TYPE_E_AC3, 253};
    private static String oneSignalApiKey = "d14a6e32-a9c4-47b9-9444-02603adeac06";

    public static String getOneSignalApiKey() {
        return oneSignalApiKey;
    }

    public static String getApiKey() {
        return apiKey;
    }
}
