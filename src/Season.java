import java.util.AbstractQueue;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;


public class Season {
	
	//not a pair anymore. oops. too lazy to rename
	private static class TeamPair{
		public Team team;
		public int depth;
		public Set<String> parents;
		public int parentWins;
		
		public TeamPair(Team t, int d, Set<String> p){
			team = t;
			depth = d;
			parents = p;
			parentWins = 0;
		}
		public TeamPair(Team t, int d, Set<String> p, int w){
			team = t;
			depth = d;
			parents = p;
			parentWins = w;
		}
	}
	
	private List<Team> teams;
	private int numWeeks;
	private Set<String> bcsTeams;
	
	//tweakable parameters
	//points earned per win and deducted per lose
	private final double PERGAME = 75;
	private final double PERLOSE = 85;
	//points for playing an FCS team
	private final double FCSWIN = 10;
	private final double FIRSTFCS = 50;
	private final double FCSLOSE = 200;
	
	private final double SOSMULT = 1.2;
	//changes the difference in bonus points for winner/loser. higher = bigger difference
	//should be between 0 and 1
	private final double QUAL_WIN_MULT = 0.5;
	private final double WIN_LOSE_MULT = 0.4; 
	private final double BAD_LOSE_MULT = 0.7;
	private final double PERGAMEPLAYED = 0;
	//decrease this number to speed up run time. 
	private final int MAXDEPTH = 2;
	private final boolean DEBUG = false;
	private final String DEBUG_TEAM = "Texas A&M";
	//---------------------

	//private Set<Game> played_games;
	
	public Season(List<Team> teams, int numWeeks, Set<String> bcsTeams) {
		super();
		this.teams = teams;
		this.numWeeks = numWeeks;
		this.bcsTeams = bcsTeams;
	}
	public void calcRankings(){
		if(!this.DEBUG){
		for(Team team : teams){
			team.setRankingScore(calcRankBFS(team));
		}
		}
		else{
		//
		Team team = this.getTeamByName(this.DEBUG_TEAM);
		team.setRankingScore(calcRankBFS(team));
		//
		}
		
		Collections.sort(teams);
	}
	public void printTopTeams(){
		for(int i=0; i<30; i++){
			System.out.println((i+1) + ") " + teams.get(i).getName() + " (" + teams.get(i).getWins() + "-" + teams.get(i).getLoses() + ")\t" + teams.get(i).getRankingScore());
		}
	}

	private double calcRank(Team t){
		Set<Game> played_games = new HashSet<Game>();
		return recScore(t, 0, played_games); 
	}
	
	/*
	 * This is the ranking algorithm. The algorithm will perform a BFS on the graph. 
	 * The root team will get/lose points for every win/lose. Additional points will be awarded/removed for margin of victory/lose.
	 * SOS is taken into consideration by awarding/removing points for every win/lose of the root's opponents and their opponents etc. with diminishing returns.
	 */
	private double calcRankBFS(Team root){
		double score = 0;
		//teams will get partial forgiveness for playing 1 FCS team
		boolean firstFCS = true;
		LinkedBlockingQueue<TeamPair> queue = new LinkedBlockingQueue<TeamPair>();
		//set of visited nodes for the BFS
		Set<String> visited = new HashSet<String>();
		visited.add(root.getName());
		//set of a node's parents/grandparents/etc. This keeps a node from doubling up on their victories.
		Set<String> parents = new HashSet<String>();
		parents.add(root.getName());
		try {
			queue.put(new TeamPair(root,0,parents));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//search until every team has been visited
		while(!queue.isEmpty()){
			TeamPair pair = queue.poll();
			for(Game g : pair.team.getGames()){
				//fewer points awarded, the deeper the node is from the root
				double marginalScore = (this.PERGAME+g.getScoreDiff())/Math.pow(numWeeks, pair.depth);
				String otherName;
				int newWins = pair.parentWins;
				//if this node won the game
				if(g.winner().equals(pair.team.getName())){
					otherName = g.loser();
					if(!pair.parents.contains(otherName)){
						//if the other team is FCS, change the marginal score
						if(!isFBS(otherName)){
							if(firstFCS){
								firstFCS = false;
								marginalScore = (this.FIRSTFCS+g.getScoreDiff())/Math.pow(numWeeks, pair.depth);
							}
							else{
								marginalScore = (this.FCSWIN+g.getScoreDiff())/Math.pow(numWeeks, pair.depth);
							}
						}
						/*
						 * The multiplier works by multiplying for every win in parent chain and dividing for every lose.
						 */
						score += Math.pow(this.SOSMULT, pair.parentWins)*marginalScore;
						newWins++;
					}
				}
				else{
					otherName = g.winner();
					if(!pair.parents.contains(otherName)){
						if(!isFBS(otherName)){
							if(firstFCS){
								firstFCS = false;
							}
							marginalScore = (this.FCSLOSE+g.getScoreDiff())/Math.pow(numWeeks, pair.depth);
						}
						/*
						 * The multiplier works by dividing for every win in parent chain and multiplying for every lose.
						 */
						score -= Math.pow(this.SOSMULT, pair.parentWins*-1)*marginalScore;
						newWins--;
					}
				}
				//Add other team to the queue
				if(!visited.contains(otherName)){
					visited.add(otherName);
					Team other = getTeamByName(otherName);
					Set<String> newParents = new HashSet<String>(pair.parents);
					newParents.add(pair.team.getName());
					try {
						queue.put(new TeamPair(other,pair.depth+1,newParents,newWins));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return score;
	}

	private double recScore(Team t, int depth, Set<Game> played_games){
		double score = 0;
		if(depth <= this.MAXDEPTH){
			Set<Game> played_this_round = new HashSet<Game>();
			for(Game g : t.getGames()){
				if(!played_games.contains(g)){
					played_games.add(g);
					played_this_round.add(g);
				}
			}
			for(int i=0; i<t.getGames().size(); i++){
				double postSeasonBonus = 1;
				Game g = t.getGames().get(i);
				if(i >= 12){
					//post season
					if(t.getGames().size() >=14){
						if(i >= 13){
							postSeasonBonus = 1.5;
						}
						else{
							postSeasonBonus = 1;
						}
					}
					else{
						postSeasonBonus = 1.5;
					}
				}
				if(!played_games.contains(g) || played_this_round.contains(g)){
					score += this.PERGAMEPLAYED;
					played_games.add(g);
					double bonus = 0;
					@SuppressWarnings("unused") //This variable helps with debugging
					double scoreDiff = 0;
					if(g.winner().equals(t.getName())){
						if(isFBS(g.loser())){
							if(postSeasonBonus == 1){
								score += this.PERGAME +(scoreDiffTransfrom(g.getScoreDiff()));
							}
							else{
								score += this.PERGAME + Math.pow((scoreDiffTransfrom(g.getScoreDiff())),postSeasonBonus-.25);
							}
							scoreDiff = scoreDiffTransfrom(g.getScoreDiff());
						}
						else{
							score += this.FCSWIN +  scoreDiffTransfrom(g.getScoreDiff()); //(((double)this.FCSWIN)/((double)this.PERGAME)) *
							scoreDiff = scoreDiffTransfrom(g.getScoreDiff()); //(((double)this.FCSWIN)/((double)this.PERGAME)) *
						}
						bonus = bonusTransform(recScore(getTeamByName(g.loser()), depth+1,new HashSet<Game>(played_games)));
						if(bonus >= 0){
							bonus *= (1+this.QUAL_WIN_MULT)*postSeasonBonus;//*Math.pow(scoreDiff,.25);
						}
						else{
							bonus *= (1-this.WIN_LOSE_MULT);
						}
						score += bonus;
					}
					else{
						if(isFBS(g.winner())){
							score -= (this.PERLOSE*postSeasonBonus + loserScoreDiffTransfrom(g.getScoreDiff(),postSeasonBonus));
						}
						else{
							//scoreDiff is not changed here, unlike for winner to add even more punishment for losing to FCS
							score -= this.FCSLOSE + loserScoreDiffTransfrom(g.getScoreDiff());
						}
						bonus = bonusTransform(recScore(getTeamByName(g.winner()), depth+1,new HashSet<Game>(played_games)));
						if(bonus >= 0){
							bonus *= (1-this.WIN_LOSE_MULT);
						}
						else{
							bonus *= (1+this.BAD_LOSE_MULT)*postSeasonBonus;
						}
						score += bonus;
					}
				}
			}
		}
		return score;
	}

	/* tweakable parameter
	 * Games further down the tree should be worth fewer points.
	 */
	private double bonusTransform(double bonus){ 
		return bonus/((double)(this.numWeeks));
	}

	/* tweakable parameter
	 * score diff should not give too many points in a blowout
	 */
	private double scoreDiffTransfrom(int scoreDiff){ 
		return Math.pow((double)scoreDiff,0.8);
	}
	private double loserScoreDiffTransfrom(int scoreDiff){ 
		return Math.pow((double)scoreDiff,1.1);
	}
	private double loserScoreDiffTransfrom(int scoreDiff, double postSeasonBonus){ 
		return Math.pow(((double)scoreDiff)*postSeasonBonus,1.1);
	}
	
	private Team getTeamByName(String name){
		return getTeamByName(this.teams, name);
	}
	public static Team getTeamByName(List<Team> teams, String name){
		for(Team t : teams){
			if(name.equals(t.getName())){
				return t;
			}
		}
		return new Team("This is bad error handling");
	}
	private boolean isFBS(String name){
		if(this.bcsTeams.contains(name)){
			return true;
		}
		return false;
	}
}
