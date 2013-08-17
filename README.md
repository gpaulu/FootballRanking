FootballRanking
===============

Algorithm to rank college football teams

This Algorithm is designed to walk through every game played to determain a team's rank. Games with fewer degrees of separation from a team will contribute more to that team's score. The Algorithm takes into acount which team won the game and which lost. It also takes into account the margin of victory. There are special penaties for playing FCS teams.

This program is designed to read game data input from this source: http://prwolfe.bol.ucla.edu/cfootball/home.htm

Known Issues
============

- Parts of the user configuration are not very clean
- Game data and BCS team data come from different sources. There may still be differences that cause FBS teams to be counted as FCS teams
- Winner of the champ game might not win depending on user configuration.
