package ulb.message.response.gameInfo;

import ulb.message.response.Response;

import java.util.List;

public class LogsResponse extends Response {
	private final List<Integer> HpsAfterFirstAction;
	private final List<String> logs;

	public LogsResponse(List<Integer> HpsAfterFirstAction, List<String> logs) {
		this.HpsAfterFirstAction = HpsAfterFirstAction;
		this.logs = logs;
	}

	public List<Integer> getHpsAfterFirstAction() { return this.HpsAfterFirstAction; }

	public List<String> getLogs() { return this.logs; }
}
