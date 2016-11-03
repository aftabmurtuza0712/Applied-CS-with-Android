package com.google.engedu.anagrams;

import android.nfc.Tag;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 3;
    private static final int DEFAULT_WORD_LENGTH = 5;
    private static final int MAX_WORD_LENGTH = 7;
    private int wordLength;
    private Random random = new Random();
    private ArrayList<String> wordList;
    private HashSet<String> wordSet;
    private HashMap<String,ArrayList<String>> lettersToWord;
    private HashMap<Integer,ArrayList<String>> sizeToWords;
    public AnagramDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        String line;

        wordList=new ArrayList<String>();
        wordSet=new HashSet<String>();
        lettersToWord=new HashMap<String, ArrayList<String>>();
        sizeToWords=new HashMap<Integer,ArrayList<String>>();
        wordLength=DEFAULT_WORD_LENGTH;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            int len=word.length();
            wordList.add(word);
            wordSet.add(word);
            String sortwrd=new String(sortLetters(word));
            ArrayList<String> al;   //Refernce variable ArrsyList
            if(lettersToWord.containsKey(sortwrd))  //if key already present in hashmap
            {
                al=lettersToWord.get(sortwrd);
                al.add(word);
            }
            else
            {
                al=new ArrayList<String>();
                al.add(word);
                lettersToWord.put(sortwrd,al);
            }

            //Adding in sizetoLetters HASHMAP
            if(sizeToWords.containsKey(len))  //if key already present in hashmap
            {
                sizeToWords.get(len).add(word);
            }
            else
            {

                al=new ArrayList<String>();
                al.add(word);
                sizeToWords.put(len,al);
            }


        }
    }

    public boolean isGoodWord(String word, String base) {   //rear and ear
        //LOGIC
        if(wordSet.contains(word))
        {
            int size=base.length();//3
            int len=word.length();//4
            for(int i=0;i<len;i+=1)
            {
                if(i+size>len)return true;
                if(word.substring(i,i+size).equalsIgnoreCase(base))return false;

            }
            return true;
        }
        return false;
    }

    public ArrayList<String> getAnagrams(String targetWord) {
            Log.i("TAG","getAnagrams for :  "+targetWord);
        String srtword = sortLetters(targetWord);
        if(lettersToWord.containsKey(srtword)) {
                 //Sorting letters to find anagrams
            ArrayList<String> result = new ArrayList<>(lettersToWord.get(srtword));
            for (String s : result) {
                Log.i("TAG", s);
            }
            return result;
        }
        else return new ArrayList<String>() ;

    }

    public ArrayList<String> getAnagramsWithOneMoreLetter(String word) {

        ArrayList<String> result = new ArrayList<String>();
        String s=new String(word);
            Log.i("TAG","Word:   "+s);
        for(char c='a';c<='z';c++)
        {
            String snew=s+c;
            Log.i("TAG","AddedWord:   "+snew);
            if(lettersToWord.containsKey(sortLetters(snew)))
            {
                ArrayList<String> al=new ArrayList<>(getAnagrams(snew));
                Iterator iter=al.iterator();
                while (iter.hasNext())
                {
                    String s1=(String) iter.next();
                    Log.i("TAG","Anagram:"+s1);
                    if(isGoodWord(s1,word))result.add(s1);
                }
            }
        }
        for(String s3:result)
        Log.d("Tag",s3);
        return result;
    }

    public String pickGoodStarterWord()
    {
        boolean found=false;
        String s="NoWord";
            for(int i=wordLength;!found;i++)
            {
                ArrayList<String> al=sizeToWords.get(i);
                if(al==null)continue;
                int random_index=(int)(Math.random()*al.size());
               // while(!al.contains(random_index)){random_index=(int)(Math.random()*al.size());}
                Log.i("TAG1","I"+i+"Size"+al.size()+"Random Index"+random_index);
                int starting_index=random_index;
                boolean present=false;
                do
                {
                    if(getAnagrams(al.get(random_index)).size()>=MIN_NUM_ANAGRAMS)
                    {
                        present=true;
                        found=true;
                        s=al.get(random_index);
                    }
                    else
                    {
                        random_index=(random_index+1)%al.size();
                        Log.i("TAG1","I"+i+"Size"+al.size()+"Random Index"+random_index);
                    }
                    if(random_index==starting_index)
                    {
                        present=true;
                    }
                }while(!present);

            }
        wordLength=(wordLength+1)%MAX_WORD_LENGTH;
        return s;
    }
    private String sortLetters(String s)
    {
        char[] c=s.toCharArray();
        Arrays.sort(c);
        s=new String(c);
        return s;
    }
}
