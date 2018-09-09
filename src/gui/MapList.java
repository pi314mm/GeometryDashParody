package gui;

public class MapList{
	private static String[] mapList = {"Level 1", "Level 2"};
	private static String[] songList = {"popcorn.wav", "The Gummy Bear Song.wav"};
	private static String[] fileNames = {"level1.txt", "level2.txt"};
	
	public static void displayGui(String map){
		for(int i = 0; i < fileNames.length; i++){
			if(mapList[i].equals(map)){
				new GUI(songList[i],fileNames[i]).start();
				return;
			}
		}
	}
	
	public static String[] maps(){
		return mapList;
	}
	
}
