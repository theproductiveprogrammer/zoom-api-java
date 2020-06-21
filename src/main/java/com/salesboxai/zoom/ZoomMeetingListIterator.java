package com.salesboxai.zoom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ZoomMeetingListIterator implements Iterator<ZoomMeetingInfo> {
	ZoomAPI za;
	String user;
	ZoomPageMeetingList firstPage;
	ZoomPageMeetingList currPage;
	Integer curr;
	Integer ndx;
	Integer pgno;

	public ZoomMeetingListIterator(ZoomAPI za, String user) throws ZoomAPIException {
		this.za = za;
		this.user = user;

		pgno = 0;
		curr = 0;
		ndx = 0;

		firstPage = getNextPage();
		currPage = firstPage;
	}

	public Integer total() {
		return firstPage.total_records;
	}

	@Override
	public boolean hasNext() {
		return curr < firstPage.total_records;
	}

	@Override
	public ZoomMeetingInfo next() {
		if(ndx >= currPage.page_size) {
			try {
				currPage = getNextPage();
				ndx = 0;
			} catch(ZoomAPIException e) {
				throw new NoSuchElementException(e.toString());
			}
		}
		ZoomMeetingInfo r = currPage.meetings[ndx];
		ndx++;
		curr++;
		return r;
	}

	private ZoomPageMeetingList getNextPage() throws ZoomAPIException {
		String url = za.endpoint("/users/" + user + "/meetings");
		pgno++;
		String params = "type=scheduled&page_number=" + pgno;
		return za.get(url + "?" + params, ZoomPageMeetingList.class);
	}

}
