FootballRanking
===============

Algorithm to rank college football teams

This Algorithm is designed to walk through every game played to determain a team's rank. Games with fewer degrees of separation from a team will contribute more to that team's score. The Algorithm takes into acount which team won the game and which lost. It also takes into account the margin of victory. There are special penaties for playing FCS teams.

This program is designed to read game data input from this source: http://prwolfe.bol.ucla.edu/cfootball/home.htm

Known Issues
============
- Games are sometimes counted more than once. The current tree search is DFS. This should be changed to BFS for best results. Perhaps some kind of iterative deepening could be helpful.
- Parts of the user configuration are not very clean
- Slow - I think the switch to BFS and no game duplication should fix this
- Playing an FCS team causes a massive pentalty. This is necessary to keep undefeated FCS teams out of the top 30, however, it skews the FBS rankings to favor teams who do not play FCS opponets. Maybe 1 FCS game should be forgiven.
- Game data and BCS team data come from different sources. There may still be differences that cause FBS teams to be counted as FCS teams
