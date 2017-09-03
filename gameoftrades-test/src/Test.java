import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Test {

	private static HashMap<Character,String> dataMap = new HashMap<Character,String>();
	
	public static void main(String[] args) throws IOException{
		
		dataMap.put('Z', "Sea");
		dataMap.put('R', "Mountains");
		
		File file = new File("res/westeros-kaart.txt");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		String s;
		int stage = 0;
		int i = 0;
		int i1 = 0;
		
		String[][] map = null;
		HashMap<String,Castle> castles = new HashMap<String,Castle>();
		Set<Trade> tradeSet = new HashSet<Trade>();
		
		while((s = reader.readLine()) != null){
			switch(stage){
			default: break;
			case 0:
				String[] mapSize = s.split(",");
				map = new String[Integer.valueOf(mapSize[0])][Integer.valueOf(mapSize[1])];
				stage = 1;
				break;
				
			case 1:
				char[] dataArray = s.toCharArray();
				for(int j = 0; j < dataArray.length; j++){
					map[j][i] = dataMap.get(dataArray[j]);
				}
				i++;
				if(i == map[0].length){
					stage = 2;
					i = 0;
				}
				break;
			
			case 2:
				i1 = Integer.valueOf(s);
				stage = 3;
				break;
				
			case 3:
				
				String[] castleData = s.split(",");
				int castlePosX = Integer.valueOf(castleData[0]);
				int castlePosY = Integer.valueOf(castleData[1]);
				String castleName = castleData[2];
				castles.put(castleName, new Castle(castleName, castlePosX, castlePosY));
				
				i++;
				if(i == i1){
					stage = 4;
					i = 0;
					i1 = 0;
				}
				break;
				
			case 4:
				i1 = Integer.valueOf(s);
				stage = 5;
				break;
				
			case 5:
				
				String[] tradeData = s.split(",");
				
				String castle = tradeData[0];
				TradeType tradeType = TradeType.valueOf(tradeData[1]);
				String product = tradeData[1];
				int priority = Integer.valueOf(tradeData[3]);
				
				Trade trade = new Trade(castles.get(castle), tradeType, product, priority);
				tradeSet.add(trade);
				
				i++;
				if(i == i1){
					stage = 6;
					i = 0;
					i1 = 0;
				}
				break;
			}
		}
		
	}
	
	private static class Castle {
		
		private final String name;
		private final int posX;
		private final int posY;
		
		public Castle(String name, int x, int y){
			this.name = name;
			this.posX = x;
			this.posY = y;
		}
		
		public String getName(){
			return name;
		}
		
		public int getPosX(){
			return posX;
		}
		
		public int getPosY(){
			return posY;
		}
	}
	
	private static enum TradeType{
		BIEDT,VRAAGT;
	}
	
	private static class Trade implements Comparable<Trade>{

		private final Castle castle;
		private final TradeType type;
		private final String product;
		private final int priority;
		
		public Trade(Castle castle, TradeType type, String product, int priority){
			this.castle = castle;
			this.type = type;
			this.product = product;
			this.priority = priority;
		}
		
		@Override
		public int compareTo(Trade other) {
			return priority - other.priority;
		}
		
		public Castle getCastle(){
			return castle;
		}
		
		public TradeType getTradeType(){
			return type;
		}
		
		public String getProduct(){
			return product;
		}
	}
}
