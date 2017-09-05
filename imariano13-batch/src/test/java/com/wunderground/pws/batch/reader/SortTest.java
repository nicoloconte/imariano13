package com.wunderground.pws.batch.reader;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Test;

import com.wunderground.pws.model.entities.MoonPhase;

public class SortTest {

	@Test
	public void test(){
		List<MoonPhase> list = new ArrayList<MoonPhase>();
		MoonPhase m1 = new MoonPhase();
		m1.setTimestamp(ZonedDateTime.now());
		list.add(m1);
		
		MoonPhase m2 = new MoonPhase();
		m2.setTimestamp(ZonedDateTime.now().minusDays(1));
		list.add(m2);
		
		MoonPhase m3 = new MoonPhase();
		m3.setTimestamp(ZonedDateTime.now().plusDays(1));
		list.add(m3);

		Collections.sort(list, new Comparator<MoonPhase>() {
			@Override
			public int compare(MoonPhase o1, MoonPhase o2) {
				if(o1.getTimestamp().isAfter(o2.getTimestamp())){
					return 1;
				}
				return -1;
			}
		});
		for (MoonPhase moonPhase : list) {
			System.out.println(moonPhase);
		}
	}
}
