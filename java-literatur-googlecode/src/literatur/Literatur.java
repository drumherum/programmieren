/**
 * java-literatur: literatur.Literatur.java
 * 
 * 06.03.2009, Harald R. Haberstroh
 */

package literatur;

import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Verwaltung einer Literaturliste in einer RandomAccess-Datei.
 * 
 * @author Harald R. Haberstroh (hp)
 * 
 */
public class Literatur {

  private RandomAccessFile file;
  private long delPos = -1; // Position des zuletzt gelöschten Eintrags

  /**
   * @param file
   */
  public Literatur(RandomAccessFile file) {
    this.file = file;
  }

  /**
   * Neuen Eintrag aufnehmen. Zuerst gelöschten suchen und ersetzen, falls dies
   * nicht möglich, dann hinten anhängen. Verwende zunächst delPos, damit's
   * schneller geht.
   * 
   * @param eintrag
   * @throws IOException
   *           Ein-/Ausgabefehler
   */
  public void neuerEintrag(Eintrag eintrag) throws IOException {
    long pos = 0;
    if (file.length() > 0) {
      if (delPos < 0) { // habe keinen aktuell gelöschten Eintrag
        // suche freien (gelöschten) Eintrag
        Eintrag e = new Eintrag();
        file.seek(pos);
        e.read(file);
        while (pos < file.length() && !e.istGeloescht()) {
          pos += Eintrag.eintragLen();
          file.seek(pos);
          if (pos < file.length()) { // sonst IOException am Ende der Datei
            e.read(file);
          }
        }
      } else { // habe gelöschten Eintrag
        pos = delPos;
        delPos = -1;
      }
    }
    // entweder gelöschten Eintrag gefunden oder Ende der Datei
    file.seek(pos);
    // überschreibt Datensatz an der Stelle (bzw. hängt an...)
    eintrag.write(file);
  }

  /**
   * lies einen Datensatz aus der Datei.
   * 
   * @param nummer
   *          Nummer des Datensatzes
   * @return Neuer Eintrag oder null, falls nicht existent
   * @throws IOException
   *           Ein-/Ausgabefehler
   */
  public Eintrag lese(int nummer) throws IOException {
    long pos = nummer * Eintrag.eintragLen();
    if (pos < file.length()) {
      file.seek(pos);
      Eintrag eintrag = new Eintrag();
      eintrag.read(file);
      if (!eintrag.istGeloescht()) {
        return eintrag;
      } else {
        if (delPos < 0) {
          delPos = pos; // merke mir gelöschten Eintrag
        }
        return null;
      }
    } else {
      return null;
    }
  }

  /**
   * lösche Datensatz. Tue nichts, wenn der DAtensatz nicht existiert.
   * 
   * @param nummer
   *          Datensatznummer
   * @throws IOException
   *           Ein-/AUsgabefehler
   */
  public void loesche(int nummer) throws IOException {
    long pos = nummer * Eintrag.eintragLen();
    if (pos < file.length()) {
      file.seek(pos);
      Eintrag eintrag = new Eintrag();
      eintrag.read(file);
      eintrag.loeschen();
      file.seek(pos);
      eintrag.write(file);
      delPos = pos; // merke mir Stelle, damit nächster Satz schnell gefunden
    }
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub

  }

}
