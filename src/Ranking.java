import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;


public class Ranking {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.print("Data Source File Name: ");
		Scanner kb = new Scanner(System.in);
		String dataFileName = kb.nextLine();
		System.out.print("What week of college football is it? ");
		int numWeeks = kb.nextInt();
		kb.close();
		
		System.out.println("Parsing data.");
		Set<String> bcsTeams = new HashSet<String>();
		Scanner bcs = new Scanner(Ranking.class.getResourceAsStream("data\\FBS_Teams.txt"));
		while(bcs.hasNext()){
			bcsTeams.add(bcs.nextLine().trim());
		}
		bcs.close();
		Scanner data;
		try {
			data = new Scanner(new File("data\\" + dataFileName));
			List<Team> teams = new ArrayList<Team>();
			Set<String> namesSoFar = new HashSet<String>();
			try{
				while(data.hasNext()){
					String date = data.next();
					//throw away neutral site data and make sure we are on a new line
					while(!date.matches("\\d\\d-\\D\\D\\D-\\d\\d")){
						if(data.hasNext()){
							date = data.next();
						}
						else{
							throw new DoneReadingException("EndOfFile");
						}
					}
					String homeName = data.next();
					String next = data.next();
					while(!isPosNumeric(next)){
						homeName = homeName.concat(" " + next);
						next = data.next();
					}
					int homeScore = Integer.parseInt(next);
					String awayName = data.next();
					next = data.next();
					while(!isPosNumeric(next)){
						awayName = awayName.concat(" " + next);
						next = data.next();
					}
					int awayScore = Integer.parseInt(next);
					Game game = new Game(homeName, homeScore, awayName, awayScore);
					if(!namesSoFar.contains(homeName)){
						Team t = new Team(homeName);
						t.addGame(game);
						teams.add(t);
						namesSoFar.add(homeName);
					}
					else{
						Team t = Season.getTeamByName(teams, homeName);
						t.addGame(game);
					}
					if(!namesSoFar.contains(awayName)){
						Team t = new Team(awayName);
						t.addGame(game);
						teams.add(t);
						namesSoFar.add(awayName);
					}
					else{
						Team t = Season.getTeamByName(teams, awayName);
						t.addGame(game);
					}
				}
			} catch (DoneReadingException e){
				//don't do anything
			} finally {
				System.out.println("Done with data parsing. Starting number crunching.");
				Season computer = new Season(teams, numWeeks, bcsTeams);
				computer.calcRankings();
				System.out.println("Done computing.\n");
				computer.printCustomTeamsRedditTable();
				data.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
	}
	
	public static boolean isPosNumeric(String str)
	{
	    for (char c : str.toCharArray())
	    {
	        if (!Character.isDigit(c)) return false;
	    }
	    return true;
	}

}
