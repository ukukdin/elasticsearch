package nurier.scraping.common.constant;

public class NpasFieldNames {

	public static final String[][] WEBLOG = new String[][]{
		{"date", "접속 일자"},
		{"name", "호스트 이름"},
		{"ip", "접속 IP"},
        {"url", "URL"},
        {"cookie", "Cookie"},
        {"agent", "User-Agent"},
        {"referer", "Referer"},
        {"clientID", "Client-ID"}
	};
	
	/**
	 * 조회 키 값, 설명, number format 사용유무
	 */
	public static final String[][] METRIC = new String[][]{
		{"date", "시간", "N"},
		{"hostname", "호스트 이름", "N"},
        {"cpuCore", "CPU Core", "N"},
        {"cpuSystemPct", "커널 CPU 사용량 (%)", "N"},
        {"cpuUserPct", "사용자 CPU 사용량 (%)", "N"},
        {"cpuPctAmount", "총 CPU 사용량 (%)", "N"},
        {"memoryByte", "메모리 사용량 (byte)", "Y"},
        {"memoryPct", "메모리 사용량 (%)", "N"},
        {"memoryTotal", "총 메모리 (byte)", "Y"},
        {"diskReadByte", "누적 디스크 Read (byte)", "Y"},
        {"diskWriteByte", "누적 디스크 Write (byte)", "Y"},
        {"diskReadByteBefore", "1분전 누적 디스크 Read (byte)", "Y"},
        {"diskWriteByteBefore", "1분전 누적 디스크 Write (byte)", "Y"},
        {"diskReadByteAmount", "1분간 디스크 Read TPS (mb/s)", "N"},
        {"diskWriteByteAmount", "1분간 디스크 Write TPS (mb/s)", "N"},
        {"networkInByte", "누적 네트워크 수신량 (byte)", "Y"},
        {"networkOutByte", "누적 네트워크 송신량 (byte)", "Y"},
        {"networkInByteBefore", "1분전 누적 네트워크 수신량 (byte)", "Y"},
        {"networkOutByteBefore", "1분전 누적 네트워크 송신량 (byte)", "Y"},
        {"networkInByteAmount", "1분간 네트워크 수신 TPS (mb/s)", "N"},
        {"networkOutByteAmount", "1분간 네트워크 송신 TPS (mb/s)", "N"}
	};
}
