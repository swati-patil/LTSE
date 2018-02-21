/**
 * 
 */
package com.ltse.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import com.ltse.util.Constants;
import com.ltse.util.Utyls;

/**
 * @author SWATI KHARKAR
 *
 */
public class ProcessOrders {

	/**
	 * @param args
	 */
	static Properties prop;
	static StringBuilder BASE_PATH;
	static {
		BASE_PATH = new StringBuilder(System.getProperty("user.dir"));
		BASE_PATH.append(File.separator).append(Constants.SOURCEFILEPATH)
			.append(File.separator);
	}
	static StringBuilder OUTPUT_PATH;
	static {
		OUTPUT_PATH = new StringBuilder(System.getProperty("user.dir"));
		OUTPUT_PATH.append(File.separator).append(Constants.OUTPUTFILEPATH)
			.append(File.separator);
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		loadProperties();
		StringBuilder symbolsFile = new StringBuilder(BASE_PATH).append(prop.getProperty("symbols"));
		StringBuilder firmsFile = new StringBuilder(BASE_PATH).append(prop.getProperty("brokers"));
		Set<String> symbols = FileProcessor.readTextFile(symbolsFile.toString());
		Set<String> firms = FileProcessor.readTextFile(firmsFile.toString());
		processOrders(symbols, firms);
	}

	static void processOrders(Set<String> tickers, Set<String> brokers) {
		BufferedReader br = null;
		FileReader fr = null;
		StringBuilder orders = new StringBuilder(BASE_PATH).append(prop.getProperty("trades"));
		TreeSet<String> rejectedOrders =  new TreeSet<String>();
		TreeSet<String> acceptedOrders = new TreeSet<String>();
		StringBuilder rejected_path = new StringBuilder(OUTPUT_PATH).append(prop.getProperty("rejected_orders"));
		StringBuilder accepted_path = new StringBuilder(OUTPUT_PATH).append(prop.getProperty("accepted_orders"));
		try {
			fr = new FileReader(orders.toString());
			br = new BufferedReader(fr);

			String line;
			Map<String, List<Integer>> brokerMap = new HashMap<String, List<Integer>>();
			Map<String, List<Integer>> marginalbrokerMap = new HashMap<String, List<Integer>>();
			Map<String, String> timedBrokerMap = new HashMap<String, String>();
			
			int counter = 0;
			while ((line = br.readLine()) != null) {
				if (counter > 0) { //ignore header file
					List<Integer> values = new ArrayList<Integer>();
					List<Integer> rangeValues = new ArrayList<Integer>();
					TradesProcessor processor = new TradesProcessor(line);
					if (processor.isValidOrder()) {
						String datetime = processor.getTime();
						String broker = processor.getBroker();
						int id = processor.getTradeId();
						String symbol = processor.getSymbol();
						if (tickers.contains(symbol)) {
							if (brokerMap.containsKey(broker)) {
								values = brokerMap.get(broker);
							}
							// check for unique trade id
							if (values.contains(id)) {
								// print line in rejected orders
								//System.out.println("repeat id ** " + line);
								rejectedOrders.add(line);
							} else {
								// check for start time per broker
								if (timedBrokerMap.containsKey(broker)) {
									String time = timedBrokerMap.get(broker);
									if (!Utyls.isDateWithinRange(time, datetime)) {
										timedBrokerMap.put(broker, datetime);
										rangeValues = new ArrayList<Integer>();
										marginalbrokerMap.put(broker, rangeValues);
									}
								} else {
									timedBrokerMap.put(broker, datetime);
								}

								if (Utyls.isDateWithinRange(timedBrokerMap.get(broker), datetime)) {
									if (marginalbrokerMap.containsKey(broker)) {
										rangeValues = marginalbrokerMap.get(broker);
										if (rangeValues.size() < 4) {
											rangeValues.add(id);
										} else {
											// print line in rejected orders
											//System.out.println("more than 3 in a minute ** " + 
													//line + start_date);
											rejectedOrders.add(line);
											rangeValues = new ArrayList<Integer>();
										}
									} else {
										values.add(id);
										brokerMap.put(broker, values);
										marginalbrokerMap.put(broker, new ArrayList<Integer>());
									}
								} else {
									rangeValues = new ArrayList<Integer>();
								}
							}
							acceptedOrders.add(line);
						} else {
							// print line in rejected orders file
							//System.out.println("Invalid Symbol ** " + line);
							rejectedOrders.add(line);
						}
					} else {
						// print line in rejected orders file
						//System.out.println("bad line ** " + line);
						rejectedOrders.add(line);
					}
				}
				counter++;
			}
			System.out.println("total lines processed " + counter + " accepted - " + acceptedOrders.size()
				+ " rejected - " + rejectedOrders.size());
			FileProcessor.writeToFile(rejected_path.toString(), rejectedOrders);
			FileProcessor.writeToFile(accepted_path.toString(), acceptedOrders);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
	}
	
	static Properties loadProperties() {
		if (prop == null) prop = new Properties();
		
		StringBuilder configPath = new StringBuilder(BASE_PATH).append(Constants.PROPFILE);
		InputStream config;
		try {
			config = new FileInputStream(configPath.toString());	
			prop.load(config);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}
}
