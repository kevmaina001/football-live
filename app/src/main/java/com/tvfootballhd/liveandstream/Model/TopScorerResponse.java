package com.tvfootballhd.liveandstream.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class TopScorerResponse implements Serializable {
    public ArrayList<Object> errors;
    public String get;
    public Paging paging;
    public Parameters parameters;
    public ArrayList<Response> response;
    public int results;

    public ArrayList<Object> getErrors() {
        return this.errors;
    }

    public String getGet() {
        return this.get;
    }

    public Paging getPaging() {
        return this.paging;
    }

    public Parameters getParameters() {
        return this.parameters;
    }

    public ArrayList<Response> getResponse() {
        return this.response;
    }

    public int getResults() {
        return this.results;
    }

    public class Birth implements Serializable {
        public String country;
        public String date;
        public String place;

        public Birth() {
        }

        public String getCountry() {
            return this.country;
        }

        public String getDate() {
            return this.date;
        }

        public String getPlace() {
            return this.place;
        }
    }

    public class Cards implements Serializable {
        public int red;
        public int yellow;
        public int yellowred;

        public Cards() {
        }

        public int getRed() {
            return this.red;
        }

        public int getYellow() {
            return this.yellow;
        }

        public int getYellowred() {
            return this.yellowred;
        }
    }

    public class Dribbles implements Serializable {
        public int attempts;
        public Object past;
        public int success;

        public Dribbles() {
        }

        public int getAttempts() {
            return this.attempts;
        }

        public Object getPast() {
            return this.past;
        }

        public int getSuccess() {
            return this.success;
        }
    }

    public class Duels implements Serializable {
        public int total;
        public int won;

        public Duels() {
        }

        public int getTotal() {
            return this.total;
        }

        public int getWon() {
            return this.won;
        }
    }

    public class Fouls implements Serializable {
        public int committed;
        public int drawn;

        public Fouls() {
        }

        public int getCommitted() {
            return this.committed;
        }

        public int getDrawn() {
            return this.drawn;
        }
    }

    public class Games implements Serializable {
        public int appearences;
        public boolean captain;
        public int lineups;
        public int minutes;
        public Object number;
        public String position;
        public String rating;

        public Games() {
        }

        public int getAppearences() {
            return this.appearences;
        }

        public boolean isCaptain() {
            return this.captain;
        }

        public int getLineups() {
            return this.lineups;
        }

        public int getMinutes() {
            return this.minutes;
        }

        public Object getNumber() {
            return this.number;
        }

        public String getPosition() {
            return this.position;
        }

        public String getRating() {
            return this.rating;
        }
    }

    public class Goals implements Serializable {
        public int assists;
        public int conceded;
        public Object saves;
        public int total;

        public Goals() {
        }

        public int getAssists() {
            return this.assists;
        }

        public int getConceded() {
            return this.conceded;
        }

        public Object getSaves() {
            return this.saves;
        }

        public int getTotal() {
            return this.total;
        }
    }

    public class League implements Serializable {
        public String country;
        public String flag;
        public int id;
        public String logo;
        public String name;
        public int season;

        public League() {
        }

        public String getCountry() {
            return this.country;
        }

        public String getFlag() {
            return this.flag;
        }

        public int getId() {
            return this.id;
        }

        public String getLogo() {
            return this.logo;
        }

        public String getName() {
            return this.name;
        }

        public int getSeason() {
            return this.season;
        }
    }

    public class Paging implements Serializable {
        public int current;
        public int total;

        public Paging() {
        }

        public int getCurrent() {
            return this.current;
        }

        public int getTotal() {
            return this.total;
        }
    }

    public class Parameters implements Serializable {
        public String league;
        public String season;

        public Parameters() {
        }

        public String getLeague() {
            return this.league;
        }

        public String getSeason() {
            return this.season;
        }
    }

    public class Passes implements Serializable {
        public int accuracy;
        public int key;
        public int total;

        public Passes() {
        }

        public int getAccuracy() {
            return this.accuracy;
        }

        public int getKey() {
            return this.key;
        }

        public int getTotal() {
            return this.total;
        }
    }

    public class Penalty implements Serializable {
        public Object commited;
        public int missed;
        public Object saved;
        public int scored;
        public Object won;

        public Penalty() {
        }

        public Object getCommited() {
            return this.commited;
        }

        public int getMissed() {
            return this.missed;
        }

        public Object getSaved() {
            return this.saved;
        }

        public int getScored() {
            return this.scored;
        }

        public Object getWon() {
            return this.won;
        }
    }

    public class Player implements Serializable {
        public int age;
        public Birth birth;
        public String firstname;
        public String height;
        public int id;
        public boolean injured;
        public String lastname;
        public String name;
        public String nationality;
        public String photo;
        public String weight;

        public Player() {
        }

        public int getAge() {
            return this.age;
        }

        public Birth getBirth() {
            return this.birth;
        }

        public String getFirstname() {
            return this.firstname;
        }

        public String getHeight() {
            return this.height;
        }

        public int getId() {
            return this.id;
        }

        public boolean isInjured() {
            return this.injured;
        }

        public String getLastname() {
            return this.lastname;
        }

        public String getName() {
            return this.name;
        }

        public String getNationality() {
            return this.nationality;
        }

        public String getPhoto() {
            return this.photo;
        }

        public String getWeight() {
            return this.weight;
        }
    }

    public class Response implements Serializable {
        public Player player;
        public ArrayList<Statistic> statistics;

        public Response() {
        }

        public Player getPlayer() {
            return this.player;
        }

        public ArrayList<Statistic> getStatistics() {
            return this.statistics;
        }
    }

    public class Shots implements Serializable {
        public int on;
        public int total;

        public Shots() {
        }

        public int getOn() {
            return this.on;
        }

        public int getTotal() {
            return this.total;
        }
    }

    public class Statistic implements Serializable {
        public Cards cards;
        public Dribbles dribbles;
        public Duels duels;
        public Fouls fouls;
        public Games games;
        public Goals goals;
        public League league;
        public Passes passes;
        public Penalty penalty;
        public Shots shots;
        public Substitutes substitutes;
        public Tackles tackles;
        public Team team;

        public Statistic() {
        }

        public Cards getCards() {
            return this.cards;
        }

        public Dribbles getDribbles() {
            return this.dribbles;
        }

        public Duels getDuels() {
            return this.duels;
        }

        public Fouls getFouls() {
            return this.fouls;
        }

        public Games getGames() {
            return this.games;
        }

        public Goals getGoals() {
            return this.goals;
        }

        public League getLeague() {
            return this.league;
        }

        public Passes getPasses() {
            return this.passes;
        }

        public Penalty getPenalty() {
            return this.penalty;
        }

        public Shots getShots() {
            return this.shots;
        }

        public Substitutes getSubstitutes() {
            return this.substitutes;
        }

        public Tackles getTackles() {
            return this.tackles;
        }

        public Team getTeam() {
            return this.team;
        }
    }

    public class Substitutes implements Serializable {
        public int bench;
        public int in;
        public int out;

        public Substitutes() {
        }

        public int getBench() {
            return this.bench;
        }

        public int getIn() {
            return this.in;
        }

        public int getOut() {
            return this.out;
        }
    }

    public class Tackles implements Serializable {
        public int blocks;
        public int interceptions;
        public int total;

        public Tackles() {
        }

        public int getBlocks() {
            return this.blocks;
        }

        public int getInterceptions() {
            return this.interceptions;
        }

        public int getTotal() {
            return this.total;
        }
    }

    public class Team implements Serializable {
        public int id;
        public String logo;
        public String name;

        public Team() {
        }

        public int getId() {
            return this.id;
        }

        public String getLogo() {
            return this.logo;
        }

        public String getName() {
            return this.name;
        }
    }
}
