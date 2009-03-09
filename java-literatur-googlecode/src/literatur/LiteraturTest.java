/**
 * java-literatur/literatur.LiteraturTest.java
 * 
 * @author (c) 2009, Harald R. Haberstroh 09.03.2009
 */

package literatur;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import junit.framework.TestCase;

public class LiteraturTest extends TestCase {

  private static Eintrag[] testdaten = {
      // autor, erscheinungsjahr, link, seiten, titel, verlag, zeitschrift
      new Eintrag(
          "Harald R. Haberstroh",
          2009,
          "http://programmierblog.blogspot.com/2009/03/literaturverwaltung.html",
          "", "Literaturverwaltung", "", ""),
      new Eintrag("Harald R. Haberstroh", 2008,
          "http://programmierblog.blogspot.com/", "", "Programmierblog", "", ""),
      new Eintrag("Tom DeMarco", 2002, "", "", "Der Termin", "Hanser", ""),
      new Eintrag("John Burnner", 1988, "", "", "Der Schockwellenreiter",
          "Heyne", ""),
      new Eintrag("", 2009, "", "S84-S87",
          "Softwaresicherheit, die 25 gefährlichsten Programmierfehler",
          "Heise", "iX"), };

  public LiteraturTest(String name) {
    super(name);
  }

  private RandomAccessFile file;

  private Literatur literatur;

  private final String filename = "literatur.dat";

  /**
   * erzeuge Testdatei mit Testdaten
   */
  protected void setUp() throws Exception {
    super.setUp();
    file = new RandomAccessFile(filename, "rw");
    literatur = new Literatur(file);
    for (Eintrag eintrag : testdaten) {
      literatur.neuerEintrag(eintrag);
    }
    ausgabe("setUp");
  }

  /**
   * entferne Testdatei
   */
  protected void tearDown() throws Exception {
    super.tearDown();
    file.close();
    File del = new File(filename);
    del.delete();
  }

  /**
   * mache Testausgabe
   * 
   * @param titel
   * 
   * @throws IOException
   *           Ein-/Ausgabefehler
   */
  private void ausgabe(String titel) throws IOException {
    System.out.println("*** " + titel + "***");
    for (long pos = 0; pos < file.length(); pos += Eintrag.eintragLen()) {
      file.seek(pos);
      Eintrag eintrag = new Eintrag();
      eintrag.read(file);
      System.out.printf("[%s] %s (%d): %s\n", eintrag.istGeloescht() ? "X"
          : " ", eintrag.getAutor(), eintrag.getErscheinungsjahr(), eintrag
          .getTitel());
    }
  }

  /**
   * teste neuen Eintrag
   * 
   * @throws IOException
   */
  public final void testNeuerEintrag() throws IOException {
    Eintrag e = new Eintrag("AUTOR", 2009, "LINK", "SEITEN", "TITEL", "VERLAG",
        "ZEITSCHRIFT");
    literatur.neuerEintrag(e);
    Eintrag ne = literatur.lese(testdaten.length);
    assertNotNull(ne); // müsste hinten dran sein
    literatur.loesche(2);
    literatur.neuerEintrag(e);
    ne = literatur.lese(2);
    assertEquals(e.getAutor(), ne.getAutor());
    ausgabe("testNeuerEintrag");
  }

  /**
   * teste Lesen
   * 
   * @throws IOException
   */
  public final void testLese() throws IOException {
    Eintrag e = literatur.lese(0);
    assertNotNull(e);
    e = literatur.lese(testdaten.length);
    assertNull(e);
    e = literatur.lese(testdaten.length - 1);
    assertNotNull(e);
  }

  /**
   * teste Löschen
   * 
   * @throws IOException
   */
  public final void testLoesche() throws IOException {
    literatur.loesche(2);
    Eintrag e = literatur.lese(2);
    assertNull(e);
    ausgabe("testLoesche");
  }

}
