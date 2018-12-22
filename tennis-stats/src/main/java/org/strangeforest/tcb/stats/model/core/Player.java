package org.strangeforest.tcb.stats.model.core;

import java.time.*;

import org.strangeforest.tcb.util.*;

public class Player {

	public static final int START_ELO_RATING = 1500;
	
	// General
	private final int id;
	private String name;
    private String chineseName;
	private LocalDate dob;
	private LocalDate dod;
	private int age;
	private Country country;
	private String birthplace;
	private String residence;
	private int height;
	private int weight;

	// Tennis
	private String hand;
	private String backhand;
	private boolean active;
	private int turnedPro;
	private String coach;
	private String prizeMoney;

	// Social
	private String wikipedia;
	private String webSite;
	private String facebook;
	private String twitter;
	private String nicknames;

	// Titles
	private int titles;
	private int grandSlams;
	private int tourFinals;
	private int altFinals;
	private int masters;
	private int olympics;

	// Ranking
	private int currentRank;
	private int currentRankPoints;
	private int bestRank;
	private LocalDate bestRankDate;
	private int currentEloRank;
	private int currentEloRating;
	private int bestEloRank;
	private LocalDate bestEloRankDate;
	private int bestEloRating;
	private LocalDate bestEloRatingDate;
	private int goatRank;
	private int goatPoints;
	private int weeksAtNo1;

	public Player(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}


	// General

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getChineseName() {
        return this.chineseName;
    }

    public void setChineseName(String chineseName) {
        this.chineseName = chineseName;
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public LocalDate getDod() {
		return dod;
	}

	public void setDod(LocalDate dod) {
		this.dod = dod;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountryId(String countryId) {
		country = new Country(countryId);
	}

	public String getBirthplace() {
		return birthplace;
	}

	public void setBirthplace(String birthplace) {
		this.birthplace = birthplace;
	}

	public String getResidence() {
		return residence;
	}

	public void setResidence(String residence) {
		this.residence = residence;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}


	// Tennis

	public String getHand() {
		return hand;
	}

	public String getHandName() {
		switch (hand) {
			case "L": return "Left-handed";
			case "R": return "Right-handed";
			default: return null;
		}
	}

	public void setHand(String hand) {
		this.hand = hand;
	}

	public String getBackhand() {
		return backhand;
	}

	public String getBackhandName() {
		switch (backhand) {
			case "1": return "One-handed";
			case "2": return "Two-handed";
			default: return null;
		}
	}

	public void setBackhand(String backhand) {
		this.backhand = backhand;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getTurnedPro() {
		return turnedPro;
	}

	public void setTurnedPro(int turnedPro) {
		this.turnedPro = turnedPro;
	}

	public String getCoach() {
		return coach;
	}

	public void setCoach(String coach) {
		this.coach = coach;
	}

	public String getPrizeMoney() {
		return prizeMoney;
	}

	public void setPrizeMoney(String prizeMoney) {
		this.prizeMoney = prizeMoney;
	}


	// Social

	public String getWikipedia() {
		return wikipedia;
	}

	public void setWikipedia(String wikipedia) {
		this.wikipedia = wikipedia;
	}

	public String getWebSite() {
		return webSite;
	}

	public void setWebSite(String webSite) {
		this.webSite = webSite;
	}

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public String getNicknames() {
		return nicknames;
	}

	public void setNicknames(String nicknames) {
		this.nicknames = nicknames;
	}
	

	// Titles

	public int getTitles() {
		return titles;
	}

	public void setTitles(int titles) {
		this.titles = titles;
	}

	public int getGrandSlams() {
		return grandSlams;
	}

	public void setGrandSlams(int grandSlams) {
		this.grandSlams = grandSlams;
	}

	public int getTourFinals() {
		return tourFinals;
	}

	public void setTourFinals(int tourFinals) {
		this.tourFinals = tourFinals;
	}

	public int getAltFinals() {
		return altFinals;
	}

	public void setAltFinals(int altFinals) {
		this.altFinals = altFinals;
	}

	public int getMasters() {
		return masters;
	}

	public void setMasters(int masters) {
		this.masters = masters;
	}

	public int getOlympics() {
		return olympics;
	}

	public void setOlympics(int olympics) {
		this.olympics = olympics;
	}


	// Ranking

	public int getCurrentRank() {
		return currentRank;
	}

	public void setCurrentRank(int currentRank) {
		this.currentRank = currentRank;
	}

	public int getCurrentRankPoints() {
		return currentRankPoints;
	}

	public void setCurrentRankPoints(int currentRankPoints) {
		this.currentRankPoints = currentRankPoints;
	}

	public int getBestRank() {
		return bestRank;
	}

	public void setBestRank(int bestRank) {
		this.bestRank = bestRank;
	}

	public LocalDate getBestRankDate() {
		return bestRankDate;
	}

	public void setBestRankDate(LocalDate bestRankDate) {
		this.bestRankDate = bestRankDate;
	}

	public int getCurrentEloRank() {
		return currentEloRank;
	}

	public void setCurrentEloRank(int currentEloRank) {
		this.currentEloRank = currentEloRank;
	}

	public int getCurrentEloRating() {
		return currentEloRating;
	}

	public void setCurrentEloRating(int currentEloRating) {
		this.currentEloRating = currentEloRating;
	}

	public int getBestEloRank() {
		return bestEloRank;
	}

	public void setBestEloRank(int bestEloRank) {
		this.bestEloRank = bestEloRank;
	}

	public LocalDate getBestEloRankDate() {
		return bestEloRankDate;
	}

	public void setBestEloRankDate(LocalDate bestEloRankDate) {
		this.bestEloRankDate = bestEloRankDate;
	}

	public int getBestEloRating() {
		return bestEloRating;
	}

	public void setBestEloRating(int bestEloRating) {
		this.bestEloRating = bestEloRating;
	}

	public LocalDate getBestEloRatingDate() {
		return bestEloRatingDate;
	}

	public void setBestEloRatingDate(LocalDate bestEloRatingDate) {
		this.bestEloRatingDate = bestEloRatingDate;
	}

	public int getGoatRank() {
		return goatRank;
	}

	public void setGoatRank(int goatRank) {
		this.goatRank = goatRank;
	}

	public int getGoatPoints() {
		return goatPoints;
	}

	public void setGoatPoints(int goatPoints) {
		this.goatPoints = goatPoints;
	}

	public int getWeeksAtNo1() {
		return weeksAtNo1;
	}

	public void setWeeksAtNo1(int weeksAtNo1) {
		this.weeksAtNo1 = weeksAtNo1;
	}
}
