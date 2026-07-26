/**
 * Contrats d'interface vers les systemes externes (Agence Nationale de
 * Meteorologie, Inspection Phytosanitaire). Le moteur de calcul depend
 * uniquement de ces interfaces, jamais des implementations concretes
 * ({@link org.example.projet_group_with_coudy.adapter}), ce qui permet de le
 * tester avec des simulations (mocks) et de le rendre agnostique de la
 * technologie d'integration retenue.
 */
package org.example.projet_group_with_coudy.port;
