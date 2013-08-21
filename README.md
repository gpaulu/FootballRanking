FootballRanking
===============

Algorithm to rank college football teams

This Algorithm is designed to walk through every game played by every team to determine a team's score. Games with fewer degrees of separation from a team will contribute more to that team's score. This is designed to reward team who play tough schedules. The Algorithm takes into account which team won the game and which lost and the margin of victory. There are special penalties for playing FCS teams.

This program is designed to read game data input from this source: http://prwolfe.bol.ucla.edu/cfootball/home.htm

Known Issues
============

- Parts of the user configuration are not very clean
- Game data and BCS team data come from different sources. There may still be differences that cause FBS teams to be counted as FCS teams
- Winner of the champ game might not win depending on user configuration.
- Algorithm favors teams who play fewer conference games

Top 30 (2012 Final):
====================
1. Alabama (13-1)
2. Notre Dame (12-1)
3. Oregon (12-1)
4. Stanford (12-2)
5. Florida (11-2)
6. Ohio State (12-0)
7. Georgia (12-2)
8. Texas A&M (11-2)
9. South Carolina (11-2)
10. Kansas St (11-2)
11. Florida St (12-2)
12. Clemson (11-2)
13. LSU (10-3)
14. Oklahoma (10-3)
15. Northern Illinois (12-2)
16. Boise St (11-2)
17. San José St (11-2)
18. Utah St (11-2)
19. Nebraska (10-4)
20. Louisville (11-2)
21. Oregon St (9-4)
22. Texas (9-4)
23. Northwestern (10-3)
24. Michigan (8-5)
25. Tulsa (11-3)
26. Central Florida (10-4)
27. Arkansas St (10-3)
28. UCLA (9-5)
29. Cincinnati (10-3)
30. Kent St (11-3)
