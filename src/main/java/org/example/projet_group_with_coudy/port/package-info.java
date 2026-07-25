/**
 * Contrats d'interface vers les systemes externes (administration fiscale,
 * Inspection du Travail). Le moteur de calcul depend uniquement de ces
 * interfaces, jamais des implementations concretes ({@link org.example.projet_group_with_coudy.adapter}),
 * ce qui permet de le tester avec des simulations (mocks) et de le rendre
 * agnostique de la technologie d'integration retenue (HTTP, file d'attente...).
 */
package org.example.projet_group_with_coudy.port;
