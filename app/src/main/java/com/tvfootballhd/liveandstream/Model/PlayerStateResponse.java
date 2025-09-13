package com.tvfootballhd.liveandstream.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class PlayerStateResponse implements Serializable {
    public ArrayList<Object> errors;
    public String get;
    public Paging paging;
    public Parameters parameters;
    public ArrayList<Response> response;
    public int results;

    public String getGet() {
        return this.get;
    }

    public void setGet(String str) {
        this.get = str;
    }

    public Parameters getParameters() {
        return this.parameters;
    }

    public void setParameters(Parameters parameters2) {
        this.parameters = parameters2;
    }

    public ArrayList<Object> getErrors() {
        return this.errors;
    }

    public void setErrors(ArrayList<Object> arrayList) {
        this.errors = arrayList;
    }

    public int getResults() {
        return this.results;
    }

    public void setResults(int i) {
        this.results = i;
    }

    public Paging getPaging() {
        return this.paging;
    }

    public void setPaging(Paging paging2) {
        this.paging = paging2;
    }

    public ArrayList<Response> getResponse() {
        return this.response;
    }

    public void setResponse(ArrayList<Response> arrayList) {
        this.response = arrayList;
    }

    public class Birth implements Serializable {
        public String country;
        public String date;
        public String place;

        public Birth() {
        }

        public String getDate() {
            return this.date;
        }

        public void setDate(String str) {
            this.date = str;
        }

        public String getPlace() {
            return this.place;
        }

        public void setPlace(String str) {
            this.place = str;
        }

        public String getCountry() {
            return this.country;
        }

        public void setCountry(String str) {
            this.country = str;
        }
    }

    public class Cards implements Serializable {
        public int red;
        public int yellow;
        public int yellowred;

        public Cards() {
        }

        public int getYellow() {
            return this.yellow;
        }

        public void setYellow(int i) {
            this.yellow = i;
        }

        public int getYellowred() {
            return this.yellowred;
        }

        public void setYellowred(int i) {
            this.yellowred = i;
        }

        public int getRed() {
            return this.red;
        }

        public void setRed(int i) {
            this.red = i;
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

        public void setAttempts(int i) {
            this.attempts = i;
        }

        public int getSuccess() {
            return this.success;
        }

        public void setSuccess(int i) {
            this.success = i;
        }

        public Object getPast() {
            return this.past;
        }

        public void setPast(Object obj) {
            this.past = obj;
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

        public void setTotal(int i) {
            this.total = i;
        }

        public int getWon() {
            return this.won;
        }

        public void setWon(int i) {
            this.won = i;
        }
    }

    public class Fouls implements Serializable {
        public int committed;
        public int drawn;

        public Fouls() {
        }

        public int getDrawn() {
            return this.drawn;
        }

        public void setDrawn(int i) {
            this.drawn = i;
        }

        public int getCommitted() {
            return this.committed;
        }

        public void setCommitted(int i) {
            this.committed = i;
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

        public void setAppearences(int i) {
            this.appearences = i;
        }

        public int getLineups() {
            return this.lineups;
        }

        public void setLineups(int i) {
            this.lineups = i;
        }

        public int getMinutes() {
            return this.minutes;
        }

        public void setMinutes(int i) {
            this.minutes = i;
        }

        public Object getNumber() {
            return this.number;
        }

        public void setNumber(Object obj) {
            this.number = obj;
        }

        public String getPosition() {
            return this.position;
        }

        public void setPosition(String str) {
            this.position = str;
        }

        public String getRating() {
            return this.rating;
        }

        public void setRating(String str) {
            this.rating = str;
        }

        public boolean isCaptain() {
            return this.captain;
        }

        public void setCaptain(boolean z) {
            this.captain = z;
        }
    }

    public class Goals implements Serializable {
        public int assists;
        public int conceded;
        public Object saves;
        public int total;

        public Goals() {
        }

        public int getTotal() {
            return this.total;
        }

        public void setTotal(int i) {
            this.total = i;
        }

        public int getConceded() {
            return this.conceded;
        }

        public void setConceded(int i) {
            this.conceded = i;
        }

        public int getAssists() {
            return this.assists;
        }

        public void setAssists(int i) {
            this.assists = i;
        }

        public Object getSaves() {
            return this.saves;
        }

        public void setSaves(Object obj) {
            this.saves = obj;
        }
    }

    public class League implements Serializable {
        public String country;
        public String flag;
        public int id;
        public String logo;
        public String name;
        public Object season;

        public League() {
        }

        public int getId() {
            return this.id;
        }

        public void setId(int i) {
            this.id = i;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String str) {
            this.name = str;
        }

        public String getCountry() {
            return this.country;
        }

        public void setCountry(String str) {
            this.country = str;
        }

        public String getLogo() {
            return this.logo;
        }

        public void setLogo(String str) {
            this.logo = str;
        }

        public String getFlag() {
            return this.flag;
        }

        public void setFlag(String str) {
            this.flag = str;
        }

        public Object getSeason() {
            return this.season;
        }

        public void setSeason(Object obj) {
            this.season = obj;
        }
    }

    public class Paging {
        public int current;
        public int total;

        public Paging() {
        }
    }

    public class Parameters implements Serializable {
        public String id;
        public String season;

        public Parameters() {
        }

        public String getId() {
            return this.id;
        }

        public void setId(String str) {
            this.id = str;
        }

        public String getSeason() {
            return this.season;
        }

        public void setSeason(String str) {
            this.season = str;
        }
    }

    public class Passes implements Serializable {
        public int accuracy;
        public int key;
        public int total;

        public Passes() {
        }

        public int getTotal() {
            return this.total;
        }

        public void setTotal(int i) {
            this.total = i;
        }

        public int getKey() {
            return this.key;
        }

        public void setKey(int i) {
            this.key = i;
        }

        public int getAccuracy() {
            return this.accuracy;
        }

        public void setAccuracy(int i) {
            this.accuracy = i;
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

        public Object getWon() {
            return this.won;
        }

        public Object getCommited() {
            return this.commited;
        }

        public int getScored() {
            return this.scored;
        }

        public int getMissed() {
            return this.missed;
        }

        public Object getSaved() {
            return this.saved;
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

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public String getFirstname() {
            return this.firstname;
        }

        public String getLastname() {
            return this.lastname;
        }

        public int getAge() {
            return this.age;
        }

        public Birth getBirth() {
            return this.birth;
        }

        public String getNationality() {
            return this.nationality;
        }

        public String getHeight() {
            return this.height;
        }

        public String getWeight() {
            return this.weight;
        }

        public boolean isInjured() {
            return this.injured;
        }

        public String getPhoto() {
            return this.photo;
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

        public void setPlayer(Player player2) {
            this.player = player2;
        }

        public ArrayList<Statistic> getStatistics() {
            return this.statistics;
        }

        public void setStatistics(ArrayList<Statistic> arrayList) {
            this.statistics = arrayList;
        }
    }

    public class Shots implements Serializable {
        public int on;
        public int total;

        public Shots() {
        }

        public int getTotal() {
            return this.total;
        }

        public void setTotal(int i) {
            this.total = i;
        }

        public int getOn() {
            return this.on;
        }

        public void setOn(int i) {
            this.on = i;
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

        public Team getTeam() {
            return this.team;
        }

        public void setTeam(Team team2) {
            this.team = team2;
        }

        public League getLeague() {
            return this.league;
        }

        public void setLeague(League league2) {
            this.league = league2;
        }

        public Games getGames() {
            return this.games;
        }

        public void setGames(Games games2) {
            this.games = games2;
        }

        public Substitutes getSubstitutes() {
            return this.substitutes;
        }

        public void setSubstitutes(Substitutes substitutes2) {
            this.substitutes = substitutes2;
        }

        public Shots getShots() {
            return this.shots;
        }

        public void setShots(Shots shots2) {
            this.shots = shots2;
        }

        public Goals getGoals() {
            return this.goals;
        }

        public void setGoals(Goals goals2) {
            this.goals = goals2;
        }

        public Passes getPasses() {
            return this.passes;
        }

        public void setPasses(Passes passes2) {
            this.passes = passes2;
        }

        public Tackles getTackles() {
            return this.tackles;
        }

        public void setTackles(Tackles tackles2) {
            this.tackles = tackles2;
        }

        public Duels getDuels() {
            return this.duels;
        }

        public void setDuels(Duels duels2) {
            this.duels = duels2;
        }

        public Dribbles getDribbles() {
            return this.dribbles;
        }

        public void setDribbles(Dribbles dribbles2) {
            this.dribbles = dribbles2;
        }

        public Fouls getFouls() {
            return this.fouls;
        }

        public void setFouls(Fouls fouls2) {
            this.fouls = fouls2;
        }

        public Cards getCards() {
            return this.cards;
        }

        public void setCards(Cards cards2) {
            this.cards = cards2;
        }

        public Penalty getPenalty() {
            return this.penalty;
        }

        public void setPenalty(Penalty penalty2) {
            this.penalty = penalty2;
        }
    }

    public class Substitutes implements Serializable {
        public int bench;
        public int in;
        public int out;

        public Substitutes() {
        }

        public int getIn() {
            return this.in;
        }

        public int getOut() {
            return this.out;
        }

        public int getBench() {
            return this.bench;
        }
    }

    public class Tackles implements Serializable {
        public int blocks;
        public int interceptions;
        public int total;

        public Tackles() {
        }

        public int getTotal() {
            return this.total;
        }

        public void setTotal(int i) {
            this.total = i;
        }

        public int getBlocks() {
            return this.blocks;
        }

        public void setBlocks(int i) {
            this.blocks = i;
        }

        public int getInterceptions() {
            return this.interceptions;
        }

        public void setInterceptions(int i) {
            this.interceptions = i;
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

        public void setId(int i) {
            this.id = i;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String str) {
            this.name = str;
        }

        public String getLogo() {
            return this.logo;
        }

        public void setLogo(String str) {
            this.logo = str;
        }
    }
}
