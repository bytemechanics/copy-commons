/*
 * Copyright 2018 Byte Mechanics.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.bytemechanics.commons.string;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Figlet java implementation
 * Documentation:
 * <ul>
 * <li><a href="https://duckduckgo.com/?q=big+text+test&ia=answer"></a></li>
 * <li><a href="https://en.wikipedia.org/wiki/ASCII"></a></li>
 * <li><a href="http://www.jave.de/figlet/figfont.html"></a></li>
 * <li><a href="https://github.com/Marak/asciimo/issues/3"></a></li>
 * </ul>
 * @author afarre
 * @since 1.3.0
 */
public class Figlet {
	
	private static final Logger logger=Logger.getLogger(Figlet.class.getName());
	
	protected final char blank;
	protected final int height;
	protected final Map<Integer,String[]> aphabet;
	
	@SuppressWarnings("OverridableMethodCallInConstructor")
	public Figlet(final Path _figletFontFile,final Charset _charset) throws IOException{
		this(Files.newInputStream(_figletFontFile),_charset);
	}
	@SuppressWarnings("OverridableMethodCallInConstructor")
	public Figlet(final InputStream _inputStream,final Charset _charset) throws IOException{
		
		try(BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(_inputStream,_charset))){
			logger.finest("figlet::load::begin");
			this.aphabet=new HashMap<>(300);
			//Process header
			final String header=bufferedReader.readLine(); 
			if(!header.startsWith("flf2a"))
				throw new IOException("Input has not figlet font file format (.flf)");
			this.blank=header.charAt(5);
			logger.log(Level.FINE, "figlet::load::blank::{0}", this.blank);
			final String[] headerParameters=header.split(" ");
			this.height=Integer.valueOf(headerParameters[1]);
			logger.log(Level.FINE, "figlet::load::height::{0}", this.height);
			if(headerParameters.length>=6){
				for(int ic1=0;ic1<Integer.valueOf(headerParameters[5]);ic1++){
					bufferedReader.readLine();
				}
			}
			logger.log(	Level.FINER, "figlet::load::comments::{0}", Integer.valueOf(headerParameters[5]));
			logger.finer("figlet::load::ascii::begin");
			logger.finer("figlet::load::ascii::standard::begin");
			String line=bufferedReader.readLine();
			int counter=0;
			while((line!=null)&&(line.charAt(0)==' ')){
				int alphabetPos=(counter+32);
				String[] letter=new String[this.height];
				char endCharacter='@';
				for(int ic1=0;ic1<this.height;ic1++){
					logger.log(Level.FINEST, "figlet::load::ascii::standard::char::{0}::line::{1}::read::{2}", new Object[]{alphabetPos, ic1, line});
					letter[ic1]=line.replace(this.blank,' ');
					endCharacter=(ic1==0)? letter[ic1].charAt(letter[ic1].length()-1) : endCharacter;
					letter[ic1]=letter[ic1].replace(endCharacter,' ');
					line=bufferedReader.readLine();
				}
				letter[this.height-1]=letter[this.height-1].substring(0,letter[this.height-1].length()-1);
				this.aphabet.put(alphabetPos,letter);
				counter++;
			}
			logger.fine("figlet::load::ascii::standard::end");
			logger.finer("figlet::load::ascii::extended::begin");
			while((line!=null)&&(line.charAt(0)!='0')){
				int alphabetPos=Integer.valueOf(line.substring(0,line.indexOf(' ')));
				String[] letter=new String[this.height];
				char endCharacter='@';
				for(int ic1=0;ic1<this.height;ic1++){
					line=bufferedReader.readLine();
					logger.log(Level.FINEST, "figlet::load::ascii::extended::char::{0}::line::{1}::read::{2}", new Object[]{alphabetPos, ic1, line});
					letter[ic1]=line.replace(this.blank,' ');
					endCharacter=(ic1==0)? letter[ic1].charAt(letter[ic1].length()-1) : endCharacter;
					letter[ic1]=letter[ic1].replace(endCharacter,' ');
				}
				letter[this.height-1]=letter[this.height-1].substring(0,letter[this.height-1].length()-1);
				this.aphabet.put(alphabetPos,letter);
				line=bufferedReader.readLine();
			}
			logger.fine("figlet::load::ascii::extended::end");
			logger.fine("figlet::load::ascii::end");
			logger.finer("figlet::load::utf-8::begin");
			while((line!=null)&&(line.charAt(0)=='0')){
				int alphabetPos=Integer.decode(line.substring(0,line.indexOf(' ')));
				String[] letter=new String[this.height];
				char endCharacter='@';
				for(int ic1=0;ic1<this.height;ic1++){
					line=bufferedReader.readLine();
					logger.log(Level.FINEST, "figlet::load::utf-8::char::{0}::line::{1}::read::{2}", new Object[]{alphabetPos, ic1, line});
					letter[ic1]=line.replace(this.blank,' ');
					endCharacter=(ic1==0)? letter[ic1].charAt(letter[ic1].length()-1) : endCharacter;
					letter[ic1]=letter[ic1].replace(endCharacter,' ');
				}
				letter[this.height-1]=letter[this.height-1].substring(0,letter[this.height-1].length()-1);
				this.aphabet.put(alphabetPos,letter);
				line=bufferedReader.readLine();
			}
			logger.fine("figlet::load::utf-8::end");
			logger.fine("figlet::load::end");
		}
	}
	
	protected final String[] fillColumn(final String _column){

		final String[] reply=new String[this.height];
		
		for(int ic1=0;ic1<this.height;ic1++){
			reply[ic1]=_column;
		}
		
		return reply;
	}

	/**
	 * Renderizes a tab character
	 * @return list of string lines with the tab escape character
	 */
	protected String[] renderTab(){
		return fillColumn("\t");
	}
	/**
	 * Renderizes a printable character if compressed is true removes first and last column
	 * @param _character character to render
	 * @param _compressed compressing flag (remove first and last columns)
	 * @return list of string that represents the scanlines of the given character
	 */
	protected String[] renderCharacter(final char _character,final boolean _compressed){

		final String[] reply=new String[this.height];
		
		logger.log(Level.FINER, "figlet::render::char::{0}::compressed::{1}::begin", new Object[]{_character, _compressed});
		final String[] fontCharacter=this.aphabet.get((int)_character);
		for(int ic1=0;ic1<this.height;ic1++){
			if(fontCharacter!=null){
				reply[ic1]=(_compressed)? fontCharacter[ic1].substring(1,fontCharacter[ic1].length()-1) : fontCharacter[ic1];
				logger.log(Level.FINEST, "figlet::render::char::{0}::compressed::{1}::position::{2}::line::{3}::text::{4}", new Object[]{_character, _compressed, (int)_character, ic1, reply[ic1]});
			}else{
				reply[ic1]="";
			}
		}
		logger.log(Level.FINE, "figlet::render::char::{0}::compressed::{1}::end", new Object[]{_character, _compressed});
		
		return reply;
	}
	
	/**
	 * Renderizes any character if compressed is true removes first and last column
	 * @param _character character to render
	 * @param _compressed compressing flag (remove first and last columns)
	 * @return list of string that represents the scanlines of the given character
	 */
	protected String[] render(final char _character,final boolean _compressed){
		
		String[] reply;

		if(_character=='\t'){
			reply=renderTab();
		}else{
			reply=renderCharacter(_character,_compressed);
		}
		
		return reply;
	}
	/**
	 * Renderizes the given phrase if compressed is true then each character is renderized without the first and the last column
	 * @param _phrase phrase to render
	 * @param _compressed compressing flag (remove first and last columns)
	 * @return list of string that represents the scanlines of the given phrase
	 */
	protected String[] render(final String _phrase,final boolean _compressed){
		
		final String[] reply=new String[this.height];
		final StringBuilder[] cache=new StringBuilder[this.height];
		
		for(int ic1=0;ic1<this.height;ic1++){
			cache[ic1]=new StringBuilder(_phrase.length());
		}
		for(int ic1=0;ic1<_phrase.length();ic1++){
			final String[] renderedChar=render(_phrase.charAt(ic1),_compressed);
			for(int ic2=0;ic2<this.height;ic2++){
				cache[ic2].append(renderedChar[ic2]);
			}
		}
		for(int ic1=0;ic1<this.height;ic1++){
			reply[ic1]=cache[ic1].toString();
		}
		
		return reply;
	}

	/**
	 * Returns an string that represents the _phrase using the loaded font
	 * @param _phrase phrase to represent
	 * @param _compressed compressing flag (remove first and last columns)
	 * @return string that represents the phrase with the provided font
	 */
	public String print(final String _phrase,final boolean _compressed){
		return Stream.of(render(_phrase,_compressed))
				.collect(Collectors.joining("\n"));
	}
	/**
	 * Returns an string that represents the _phrase using the loaded font
	 * @param _phrase phrase to represent
	 * @return string that represents the phrase with the provided font
	 */
	public String print(final String _phrase){
		return print(_phrase,true);
	}

	/**
	 * Calculate the length of the banner
	 * @param _phrase phrase to calculate length
	 * @param _compressed if phrase must be considered compressed
	 * @return the length of the banner
	 */
	public int length(final String _phrase,final boolean _compressed){
		return _phrase.chars()
						.boxed()
						.map(charNum -> this.aphabet.get(charNum))
						.map(bannerChar -> bannerChar[0].length())
						.map(bannerCharSize -> (_compressed)? bannerCharSize-2 : bannerCharSize)
						.mapToInt(bannerCharSize -> (int)bannerCharSize)
						.sum();
	}

	/**
	 * Calculate the length of the banner without compression
	 * @param _phrase phrase to calculate length
	 * @return the length of the banner
	 */
	public int length(final String _phrase){
		return length(_phrase,true);
	}

	/**
	 * Returns an string with the same length of the _phrase but filled with _filler
	 * @param _phrase phrase to represent
	 * @param _compressed if phrase must be considered compressed
	 * @param _filler character to fill the same phrase length in the returned string
	 * @return string with the same length of the phrase corresponding banner but with only onle line filled with _filler
	 */
	public String line(final String _phrase,final boolean _compressed,final char _filler){
		final char[] reply=new char[this.length(_phrase,_compressed)];
		Arrays.fill(reply,_filler);
		return String.valueOf(reply);
	}

	/**
	 * Returns an string with the same length of the _phrase but filled with _filler without compression
	 * @param _phrase phrase to represent
	 * @param _filler character to fill the same phrase length in the returned string
	 * @return string with the same length of the phrase corresponding banner but with only onle line filled with _filler
	 */
	public String line(final String _phrase,final char _filler){
		return line(_phrase, true, _filler);
	}
}
