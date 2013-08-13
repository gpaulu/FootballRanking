FootballRanking
===============

Algorithm to rank college football teams

Known Issues
============
- Games are sometimes counted more than once. The current tree search is DFS. This should be changed to BFS for best results.
- Parts of the user configuration are not very clean
- Slow - I think the switch to BFS and no game duplication should fix this
- Playing an FCS team causes a massive pentalty. This is necessary to keep undefeated FCS teams out of the top 30, however, it skews the FBS rankings to favor teams who do not play FCS opponets. Maybe 1 FCS game should be forgiven.
- Game data and BCS team data come from different sources. There may still be differences that cause FBS teams to be counted as FCS teams
