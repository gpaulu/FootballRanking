import java.util.ArrayList;
import java.util.List;


public class Team implements Comparable<Team> {
	private String name;
	private List<Game> games;
	private double rankingScore;
	private int wins;
	private int loses;
	private boolean winsCalced;
	private boolean losesCalced;
	public String getName() {
		return name;
	}
	public List<Game> getGames() {
		return games;
	}
	public void setGames(List<Game> games) {
		this.games = games;
		this.winsCalced = false;
		this.losesCalced = false;
	}
	public void addGame(Game game){
		this.games.add(game);
		this.winsCalced = false;
		this.losesCalced = false;
	}
	public double getRankingScore() {
		return rankingScore;
	}
	public void setRankingScore(double rankingScore) {
		this.rankingScore = rankingScore;
	}
	public int getWins(){
		if(!this.winsCalced){
			int wins = 0;
			for(Game g : games){
				if(g.winner().equals(this.name)){
					wins++;
				}
			}
			this.wins = wins;
			this.winsCalced = true;
		}
		return this.wins;
	}
	public int getLoses(){
		if(!this.losesCalced){
			int loses = 0;
			for(Game g : games){
				if(g.loser().equals(this.name)){
					loses++;
				}
			}
			this.loses = loses;
			this.losesCalced = true;
		}
		return this.loses;
	}
	public Team(String name){
		this.name = name;
		this.games = new ArrayList<Game>();
		this.rankingScore = 0;
		this.winsCalced = false;
		this.losesCalced = false;
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
