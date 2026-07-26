package org.example.projet_group_with_coudy.port;

/**
 * Contrat vers l'API de l'Agence Nationale de Meteorologie (Besoin 4).
 * Le moteur ne possede pas de donnees meteorologiques : il interroge ce
 * port avec la localisation de l'exploitation pour savoir si la region a
 * subi une secheresse severe.
 */
public interface MeteorologyPort {
    boolean isSevereDrought(String location);
}
