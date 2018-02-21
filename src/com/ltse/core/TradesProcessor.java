/**
 * 
 */
package com.ltse.core;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ltse.util.Constants;

/**
 * @author SWATI KHARKAR
 *
 */
public class TradesProcessor {

	/**
	 * trades.csv has following format - 
	 * Time stamp,broker,sequence id,type,Symbol,Quantity,Price,Side
	 * symbol = 5th token in a comma separated line
	 * time = 1st token
	 * broker = 2nd token
	 * trade_id = sequence id = 3rd token
	 */
	String line;
	static String[] data;
	
	public TradesProcessor() {
		// TODO Auto-generated constructor stub
	}
	
	public TradesProcessor(String line) {
		this.line = line;
	}

	public boolean isValidOrder() {
		if (!line.isEmpty()) {
			data = splitLine(this.line);
			if (data.length == 8)
				return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	static String[] splitLine (String line2) {
		return line2.split(Constants.CSVSEPARATOR);
	}
	

	public String getTime() {
		String ts = "";
		//String[] tokens = splitLine(this.line);
		ts = data[0];
		return ts;
	}
	public String getSymbol () {
		String symbol = "";
		//if (tokens == null) tokens = splitLine();
		symbol = data[4];
		return symbol;
	}
	
	public int getTradeId () {
		int tradeId = 0;
		//if (tokens == null) tokens = splitLine();
			tradeId = Integer.parseInt(data[2]);
		return tradeId;
	}
	
	public String getBroker() {
		String broker = "";
		//if (tokens == null) tokens = splitLine();
			broker = data[1];
		return broker;
	}
	
	public static boolean isValidBroker(Set<String> brokers, String broker) {
		return (brokers.contains(broker)) ? Boolean.TRUE : Boolean.FALSE ;
	}
	
	public static boolean isBrokerIdUnique(Map<String, List<Integer>> brokerIdMap, int tradeId, String broker) {
		List<Integer> ids = brokerIdMap.get(broker);
		return (ids.contains(tradeId)) ? Boolean.FALSE : Boolean.TRUE ;
	}
}
