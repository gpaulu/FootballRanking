FootballRanking
===============

Algorithm to rank college football teams

This Algorithm is designed to walk through every game played to determain a team's rank. Games with fewer degrees of separation from a team will contribute more to that team's score. The Algorithm takes into acount which team won the game and which lost. It also takes into account the margin of victory. There are special penaties for playing FCS teams.

This program is designed to read game data input from this source: http://prwolfe.bol.ucla.edu/cfootball/home.htm

Known Issues
============

I hacked out this code one night, but I realized it has a major problem. If Texas A&M plays Alabama and Auburn in that order, they will get credit for Alabama beating Auburn but won't lose anything for Auburn losing. I need to revise the algorithm so that it does a BFS, is symetric in terms of points for winning or losing, and stops searching based on the team, not the game. Please don't use this code yet, it's horribly broken, and I need some times to figure out how to do it right. 


- Games are sometimes counted more than once. The current tree search is DFS. This should be changed to BFS for best results. Perhaps some kind of iterative deepening could be helpful.
- Parts of the user configuration are not very clean
- Slow - I think the switch to BFS and no game duplication should fix this
- Playing an FCS team causes a massive pentalty. This is necessary to keep undefeated FCS teams out of the top 30, however, it skews the FBS rankings to favor teams who do not play FCS opponets. Maybe 1 FCS game should be forgiven.
- Game data and BCS team data come from different sources. There may still be differences that cause FBS teams to be counted as FCS teams
