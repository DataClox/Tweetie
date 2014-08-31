package com.dataclox.tweetie.main;

import java.util.*;

/**
 * Created by devilo on 21/8/14.
 */
public class TweeChat {

    private HashSet<Long> roots = null;
    private HashMap<Long, Tweet> tweetMap = null;
    private TweeStruct tweeStruct = TweeStruct.getInstance();
    private TreeMap<Long, LinkedHashSet<Long>> adjacencyList = null;


    HashMap<Long, String> tweetIdVsTweetText = null;
    HashSet<Long> conversationTweetIds = null;
    HashMap<Long, HashSet<String>> tweetIdVsSet= null;


    public TweeChat() {
        roots = tweeStruct.getRoots();
        tweetMap = tweeStruct.getTweetMap();
        adjacencyList = tweeStruct.getAdjacencyList();
        tweetIdVsTweetText = new HashMap<Long, String>();

        tweetIdVsSet = new HashMap<Long, HashSet<String>>();
        conversationTweetIds = new HashSet<Long>();

    }



    public void start() {

        Random random = new Random();

        Scanner scanner = new Scanner(System.in);

        while ( true ) {

            Long maxId = null;
            float maxJC = Float.MIN_VALUE;
            float jc;

            System.out.print(">>> ");
            String humanSay = scanner.nextLine();

            //System.out.println("For line : " + humanSay);
            //System.out.println("Set is : " + stringToSet(humanSay.toLowerCase()));
            HashSet<String> humanSaySet = stringToSet(humanSay.toLowerCase());

            HashSet<String> union = new HashSet<String>();
            HashSet<String> intersection = new HashSet<String>();

            for( Long id : conversationTweetIds ) {

                union.clear();
                intersection.clear();

                union.addAll(humanSaySet);
                intersection.addAll(humanSaySet);

                HashSet<String> candidateSay = tweetIdVsSet.get(id);

                union.addAll(candidateSay);
                intersection.retainAll(candidateSay);

                jc = ((float)candidateSay.size()) * ((float) intersection.size() / (float)union.size());

                //System.out.println("JC = " + jc);

                if( jc > maxJC ) {
                    maxJC = jc;
                    maxId = id;

                    //System.out.println("maxJC = " + maxJC + " and maxId = " + maxId);
                    //System.out.println("Inter : " + intersection + " and Union : " + union);

                }

            }


            if( maxId == null ) {
                System.out.println("Okay ... I c");
                continue;
            }

            Long replyId = 0L;
            LinkedHashSet<Long> possibleReplies = adjacencyList.get(maxId);
            if( possibleReplies == null ) {
                System.out.println("Okay ... I c");
            }
            else if(possibleReplies.size() > 0 ) {

                Long[] arr = ((Long[]) possibleReplies.toArray());
                int size = arr.length;

                replyId = arr[((random.nextInt() % size) + (random.nextInt() % size)) % arr.length];
                System.out.println(tweetIdVsTweetText.get(replyId));
            }
            else {
                System.out.println(tweetIdVsTweetText.get(maxId));
            }

        }

    }

    public void initialize() {

        for( Long rootId : roots ) {

            Queue<Long> q = new LinkedList<Long>();
            q.add(rootId);

            while ( !q.isEmpty() ) {

                Long id = q.poll();

                String text = getProcessedTweet(tweetMap.get(id).getTweetText());

                if( text == null || text.length() == 0 )
                    continue;

                if( isEnglish(text) ) {
                    conversationTweetIds.add(id);
                    tweetIdVsTweetText.put(id, text);
                    tweetIdVsSet.put(id, stringToSet(text));
                }

                if( adjacencyList.containsKey(id)) {
                    for (Long childId : adjacencyList.get(id)) {
                        q.add(childId);
                    }
                }
            }

        }

        //System.out.println("Total english tweets in conversation : " + tweetIdVsTweetText.size());

    }

    private HashSet<String> stringToSet(String text) {
        HashSet<String> s = new HashSet<String>();

        String[] array = text.split(" ");

        for( String str : array )
            s.add(str);

        return s;
    }

    private String getProcessedTweet(String tweetText) {

        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;

        if(tweetText.charAt(0) == 'R' && tweetText.charAt(1) == 'T' ) {
            i = 2;
        }

        for(  ; i < tweetText.length() ; i++ ) {

            char ch = tweetText.charAt(i);

            if( ch == '@' || ch == '#' ) {

                i++;

                if( i == tweetText.length() )
                    break;

                char c = tweetText.charAt(i);

                while( Character.isLetterOrDigit(c) || c == '_' ) {
                    i++;

                    if( i == tweetText.length() )
                        break;

                    c = tweetText.charAt(i);

                }

            }
            else if( tweetText.substring(i).startsWith("http://") || tweetText.substring(i).startsWith("https://") ) {

                char c = tweetText.charAt(i);

                while( c != ' ' ) {
                    i++;

                    if( i == tweetText.length() )
                        break;

                    c = tweetText.charAt(i);

                }

            }
            else if( !( ((ch >= 'A' && ch <= 'Z') || (ch >= 'a' && ch <= 'z') || Character.isDigit(ch) ) || ch == ':' || ch == ';' || ch == '(' || ch == ')' || ch == ',' || ch == ' ') ) {

            }
            else {
                stringBuilder.append(ch);
            }

        }

        return new String(stringBuilder).replaceAll("  " , " ").trim().toLowerCase();
    }

    private boolean isEnglish(String text) {

        if( text.length() > 0 )
            return true;

        return false;
    }

}