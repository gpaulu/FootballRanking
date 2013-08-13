import java.util.ArrayList;
import java.util.List;


public class Team implements Comparable<Team> {
	private String name;
	private List<Game> games;
	private double rankingScore;
	public String getName() {
		return name;
	}
	public List<Game> getGames() {
		return games;
	}
	public void setGames(List<Game> games) {
		this.games = games;
	}
	public void addGame(Game game){
		this.games.add(game);
	}
	public double getRankingScore() {
		return rankingScore;
	}
	public void setRankingScore(double rankingScore) {
		this.rankingScore = rankingScore;
	}
	public int getWins(){
		int wins = 0;
		for(Game g : games){
			if(g.winner().equals(this.name)){
				wins++;
			}
		}
		return wins;
	}
	public int getLoses(){
		int loses = 0;
		for(Game g : games){
			if(g.loser().equals(this.name)){
				loses++;
			}
		}
		return loses;
	}
	public Team(String name){
		this.name = name;
		this.games = new ArrayList<Game>();
		this.rankingScore = 0;
	}
	@Override
	public int compareTo(Team t) {
		if(this.getRankingScore() > t.getRankingScore()){
			return -1;
		}
		else if(this.getRankingScore() < t.getRankingScore()){
			return 1;
		}
		return 0;
	}
}
