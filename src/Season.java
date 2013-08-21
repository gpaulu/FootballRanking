import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;


public class Season {
	
	//tweakable parameters
	//points earned per win and deducted per lose
	private final double PERGAME = 75;
	//points for playing an FCS team
	private final double FCSWIN = 10;
	private final double FIRSTFCS = 50;
	private final double FCSLOSE = 200;
	private final double SOSMULT = 1.2;
	private final double DEPTHMOD = 1;
	private final boolean DEBUG = false;
	private final String DEBUG_TEAM = "Texas A&M";
	//---------------------
	
	//not a pair anymore. oops. too lazy to rename
	private class TeamPair{
		private Team team;
		private int depth;
		private Set<String> parents;
		private int parentWins;
		
		public TeamPair(Team t, int d, Set<String> p){
			this(t,d,p,0);
		}
		public TeamPair(Team t, int d, Set<String> p, int w){
			team = t;
			depth = d;
			parents = p;
			parentWins = w;
		}
		public Team getTeam() {
			return team;
		}
		public int getDepth() {
			return depth;
		}
		public Set<String> getParents() {
			return parents;
		}
		public int getParentWins() {
			return parentWins;
		}
	}
	
	private List<Team> teams;
	private int numWeeks;
	private Set<String> bcsTeams;
	
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
			System.out.println((i+1) + ". " + teams.get(i).getName() + " (" + teams.get(i).getWins() + "-" + teams.get(i).getLoses() + ")\t" + teams.get(i).getRankingScore());
		}
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
			for(Game g : pair.getTeam().getGames()){
				//fewer points awarded, the deeper the node is from the root
				double marginalScore = (this.PERGAME+g.getScoreDiff())/Math.pow(numWeeks*this.DEPTHMOD, pair.getDepth());
				String otherName;
				int newWins = pair.getParentWins();
				//if this node won the game
				if(g.winner().equals(pair.getTeam().getName())){
					otherName = g.loser();
					if(!pair.getParents().contains(otherName)){
						//if the other team is FCS, change the marginal score
						if(!isFBS(otherName)){
							if(firstFCS){
								firstFCS = false;
								marginalScore = (this.FIRSTFCS+g.getScoreDiff())/Math.pow(numWeeks*this.DEPTHMOD, pair.getDepth());
							}
							else{
								marginalScore = (this.FCSWIN+g.getScoreDiff())/Math.pow(numWeeks*this.DEPTHMOD, pair.getDepth());
							}
						}
						/*
						 * The multiplier works by multiplying for every win in parent chain and dividing for every lose.
						 */
						score += Math.pow(this.SOSMULT, pair.getParentWins())*marginalScore;
						newWins++;
					}
				}
				else{
					otherName = g.winner();
					if(!pair.getParents().contains(otherName)){
						if(!isFBS(otherName)){
							if(firstFCS){
								firstFCS = false;
							}
							marginalScore = (this.FCSLOSE+g.getScoreDiff())/Math.pow(numWeeks*this.DEPTHMOD, pair.getDepth());
						}
						/*
						 * The multiplier works by dividing for every win in parent chain and multiplying for every lose.
						 */
						score -= Math.pow(this.SOSMULT, pair.getParentWins()*-1)*marginalScore;
						newWins--;
					}
				}
				//Add other team to the queue
				if(!visited.contains(otherName)){
					visited.add(otherName);
					Team other = getTeamByName(otherName);
					Set<String> newParents = new HashSet<String>(pair.getParents());
					newParents.add(pair.getTeam().getName());
					try {
						queue.put(new TeamPair(other,pair.getDepth()+1,newParents,newWins));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return score;
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
