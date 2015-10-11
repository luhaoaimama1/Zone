package com.fourmob.date;
import java.util.ArrayList;
import java.util.List;
public class Root_store {
	private List<Root_store_book> book=new ArrayList<Root_store_book>();
	private Root_store_bicycle bicycle;

	public List<Root_store_book> getBook() {
		return book;
	}

	public void setBook(List<Root_store_book> book) {
		this.book = book;
	}

	public Root_store_bicycle getBicycle() {
	return bicycle;
	}

	public void setBicycle(Root_store_bicycle bicycle) {
	this.bicycle = bicycle;
	}

}