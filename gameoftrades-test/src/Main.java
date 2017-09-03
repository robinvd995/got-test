import io.gameoftrades.model.Wereld;
import io.gameoftrades.model.lader.WereldLader;

public class Main {
	
	public static void main(String[] args) {
		
		WereldLader wereldLader = new WorldLoader();
		Wereld wereld = wereldLader.laad("res/westeros-kaart.txt");
		
		System.out.println(wereld.getKaart() + "\n---\n" + wereld.getMarkt().getAanbod() + "\n---\n" + wereld.getSteden());
	}
}
