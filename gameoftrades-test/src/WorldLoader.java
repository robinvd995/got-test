import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.gameoftrades.model.Wereld;
import io.gameoftrades.model.kaart.Coordinaat;
import io.gameoftrades.model.kaart.Kaart;
import io.gameoftrades.model.kaart.Stad;
import io.gameoftrades.model.kaart.Terrein;
import io.gameoftrades.model.kaart.TerreinType;
import io.gameoftrades.model.lader.WereldLader;
import io.gameoftrades.model.markt.Handel;
import io.gameoftrades.model.markt.HandelType;
import io.gameoftrades.model.markt.Handelswaar;
import io.gameoftrades.model.markt.Markt;

public class WorldLoader implements WereldLader{

	@SuppressWarnings("finally")
	@Override
	public Wereld laad(String resource){
		
		Wereld wereld = null;
		
		try{
			wereld = laadWereld(resource);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		finally{
			return wereld;
		}
		
	}

	private Wereld laadWereld(String fileName) throws IOException{
		
		File file = new File(fileName);
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		HashMap<String,Stad> steden = new HashMap<String,Stad>();
		List<Handel> handelList = new ArrayList<Handel>();
		Kaart kaart = null;
		
		int stage = 0;
		int i = 0;
		int i1 = 0;
		String s;
		while((s = reader.readLine()) != null){
			switch(stage){
			default: break;
			case 0:
				String[] mapSize = s.split(",");
				kaart = new Kaart(Integer.valueOf(mapSize[0]), Integer.valueOf(mapSize[1]));
				stage = 1;
				break;
				
			case 1:
				char[] dataArray = s.toCharArray();
				for(int j = 0; j < dataArray.length; j++){
					new Terrein(kaart, Coordinaat.op(j, i), TerreinType.fromLetter(dataArray[j]));
				}
				i++;
				if(i == kaart.getHoogte()){
					stage = 2;
					i = 0;
				}
				break;
			
			case 2:
				i1 = Integer.valueOf(s);
				stage = 3;
				break;
				
			case 3:
				
				String[] stadData = s.split(",");
				int castlePosX = Integer.valueOf(stadData[0]);
				int castlePosY = Integer.valueOf(stadData[1]);
				String stadNaam = stadData[2];
				steden.put(stadNaam, new Stad(Coordinaat.op(castlePosX, castlePosY), stadNaam));
				
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
				
				String stad = tradeData[0];
				HandelType handelType = HandelType.valueOf(tradeData[1]);
				Handelswaar product = new Handelswaar(tradeData[2]);
				int prijs = Integer.valueOf(tradeData[3]);
				
				handelList.add(new Handel(steden.get(stad), handelType, product, prijs));
				
				i++;
				if(i == i1){
					stage = 6;
					i = 0;
					i1 = 0;
				}
				break;
			}
			
		}
		
		reader.close();
		
		Markt markt = new Markt(handelList);
		List<Stad> stedenLijst = new ArrayList<>(steden.values());
		return new Wereld(kaart, stedenLijst, markt);
	}
}
