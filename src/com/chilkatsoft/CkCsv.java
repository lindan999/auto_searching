package com.chilkatsoft;

import com.chilkatsoft.CkString;
import com.chilkatsoft.chilkatJNI;

public class CkCsv {   
	  private long swigCPtr;   
	  protected boolean swigCMemOwn;   
	   
	  protected CkCsv(long cPtr, boolean cMemoryOwn) {   
	    swigCMemOwn = cMemoryOwn;   
	    swigCPtr = cPtr;   
	  }   
	   
	  protected static long getCPtr(CkCsv obj) {   
	    return (obj == null) ? 0 : obj.swigCPtr;   
	  }   
	   
	  protected void finalize() {   
	    delete();   
	  }   
	   
	  public synchronized void delete() {   
	    if(swigCPtr != 0 && swigCMemOwn) {   
	      swigCMemOwn = false;   
	      chilkatJNI.delete_CkCsv(swigCPtr);   
	    }   
	    swigCPtr = 0;   
	  }   
	   
	  public CkCsv() {   
	    this(chilkatJNI.new_CkCsv(), true);   
	  }   
	   
	    
	  public boolean SaveLastError(String filename) {   
	    return chilkatJNI.CkCsv_SaveLastError(swigCPtr, this, filename);   
	  }   
	   
	  public void LastErrorXml(CkString str) {   
	    chilkatJNI.CkCsv_LastErrorXml(swigCPtr, this, CkString.getCPtr(str), str);   
	  }   
	   
	  public void LastErrorHtml(CkString str) {   
	    chilkatJNI.CkCsv_LastErrorHtml(swigCPtr, this, CkString.getCPtr(str), str);   
	  }   
	   
	  public void LastErrorText(CkString str) {   
	    chilkatJNI.CkCsv_LastErrorText(swigCPtr, this, CkString.getCPtr(str), str);   
	  }   
	   
	  public String lastErrorText() {   
	    return chilkatJNI.CkCsv_lastErrorText(swigCPtr, this);   
	  }   
	   
	  public String lastErrorXml() {   
	    return chilkatJNI.CkCsv_lastErrorXml(swigCPtr, this);   
	  }   
	   
	  public String lastErrorHtml() {   
	    return chilkatJNI.CkCsv_lastErrorHtml(swigCPtr, this);   
	  }   
	   
	  public boolean LoadFile(String filename) {   
	    return chilkatJNI.CkCsv_LoadFile(swigCPtr, this, filename);   
	  }   
	   
	  public boolean SaveFile(String filename) {   
	    return chilkatJNI.CkCsv_SaveFile(swigCPtr, this, filename);   
	  }   
	   
	  public boolean SaveFile2(String filename, String charset) {   
	    return chilkatJNI.CkCsv_SaveFile2(swigCPtr, this, filename, charset);   
	  }   
	   
	  public boolean LoadFile2(String filename, String charset) {   
	    return chilkatJNI.CkCsv_LoadFile2(swigCPtr, this, filename, charset);   
	  }   
	   
	  public boolean GetCell(int row, int col, CkString outStr) {   
	    return chilkatJNI.CkCsv_GetCell(swigCPtr, this, row, col, CkString.getCPtr(outStr), outStr);   
	  }   
	   
	  public String getCell(int row, int col) {   
	    return chilkatJNI.CkCsv_getCell(swigCPtr, this, row, col);   
	  }   
	   
	  public int get_NumRows() {   
	    return chilkatJNI.CkCsv_get_NumRows(swigCPtr, this);   
	  }   
	   
	  public boolean SetCell(int row, int col, String content) {   
	    return chilkatJNI.CkCsv_SetCell(swigCPtr, this, row, col, content);   
	  }   
	   
	  public int GetNumCols(int row) {   
	    return chilkatJNI.CkCsv_GetNumCols(swigCPtr, this, row);   
	  }   
	   
	  public void get_Delimiter(CkString str) {   
	    chilkatJNI.CkCsv_get_Delimiter(swigCPtr, this, CkString.getCPtr(str), str);   
	  }   
	   
	  public String delimiter() {   
	    return chilkatJNI.CkCsv_delimiter(swigCPtr, this);   
	  }   
	   
	  public void put_Delimiter(String newVal) {   
	    chilkatJNI.CkCsv_put_Delimiter(swigCPtr, this, newVal);   
	  }   
	   
	  public boolean get_Crlf() {   
	    return chilkatJNI.CkCsv_get_Crlf(swigCPtr, this);   
	  }   
	   
	  public void put_Crlf(boolean newVal) {   
	    chilkatJNI.CkCsv_put_Crlf(swigCPtr, this, newVal);   
	  }   
	   
	  public boolean get_HasColumnNames() {   
	    return chilkatJNI.CkCsv_get_HasColumnNames(swigCPtr, this);   
	  }   
	   
	  public void put_HasColumnNames(boolean newVal) {   
	    chilkatJNI.CkCsv_put_HasColumnNames(swigCPtr, this, newVal);   
	  }   
	   
	  public int get_NumColumns() {   
	    return chilkatJNI.CkCsv_get_NumColumns(swigCPtr, this);   
	  }   
	   
	  public int GetIndex(String columnName) {   
	    return chilkatJNI.CkCsv_GetIndex(swigCPtr, this, columnName);   
	  }   
	   
	  public boolean GetColumnName(int index, CkString outStr) {   
	    return chilkatJNI.CkCsv_GetColumnName(swigCPtr, this, index, CkString.getCPtr(outStr), outStr);   
	  }   
	   
	  public String getColumnName(int index) {   
	    return chilkatJNI.CkCsv_getColumnName(swigCPtr, this, index);   
	  }   
	   
	  public boolean LoadFromString(String csvData) {   
	    return chilkatJNI.CkCsv_LoadFromString(swigCPtr, this, csvData);   
	  }   
	   
	  public boolean SaveToString(CkString outStr) {   
	    return chilkatJNI.CkCsv_SaveToString(swigCPtr, this, CkString.getCPtr(outStr), outStr);   
	  }   
	   
	  public String saveToString() {   
	    return chilkatJNI.CkCsv_saveToString(swigCPtr, this);   
	  }   
	   
	  public boolean SetColumnName(int index, String columnName) {   
	    return chilkatJNI.CkCsv_SetColumnName(swigCPtr, this, index, columnName);   
	  }   
	   
	}   