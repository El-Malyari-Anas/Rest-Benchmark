> **Benchmark de performances des Web Services REST Travail en binôme**
>
> **Objectif**

Évaluer, sur un même domaine métier et une même base de données,
l'impact des choix de stack REST sur :

- Latence (p50/p95/p99), débit (req/s), taux d'erreurs.

- Empreinte CPU/RAM, GC, threads.

- Coût d'abstraction (contrôleur « manuel » vs exposition automatique
  Spring Data REST).

> **T0 --- Configuration matérielle & logicielle**

<table>
<colgroup>
<col style="width: 51%" />
<col style="width: 48%" />
</colgroup>
<thead>
<tr class="header">
<th><blockquote>
<p><strong>Élément</strong></p>
</blockquote></th>
<th><blockquote>
<p><strong>Valeur</strong></p>
</blockquote></th>
</tr>
</thead>
<tbody>
<tr class="odd">
<td><blockquote>
<p>Machine (CPU, cœurs, RAM)</p>
</blockquote></td>
<td><p>Intel(R) Core(TM) i5-10310U CPU @ 1.70GHz (2.21 GHz)</p>
<p>16,0 Go (15,8 Go utilisable)</p></td>
</tr>
<tr class="even">
<td><blockquote>
<p>OS / Kernel</p>
</blockquote></td>
<td>Windows 11 Professionnel 24H2</td>
</tr>
<tr class="odd">
<td><blockquote>
<p>Java version</p>
</blockquote></td>
<td><p>java 24.0.1 2025-04-15</p>
<p>Java(TM) SE Runtime Environment (build 24.0.1+9-30)</p>
<p>Java HotSpot(TM) 64-Bit Server VM (build 24.0.1+9-30, mixed mode,
sharing)</p></td>
</tr>
<tr class="even">
<td><blockquote>
<p>Docker/Compose versions</p>
</blockquote></td>
<td>4.44.3 (202357)</td>
</tr>
<tr class="odd">
<td><blockquote>
<p>PostgreSQL version</p>
</blockquote></td>
<td>Postgres 14</td>
</tr>
<tr class="even">
<td><blockquote>
<p>JMeter version</p>
</blockquote></td>
<td>5.6.3</td>
</tr>
<tr class="odd">
<td><blockquote>
<p>Prometheus / Grafana / InfluxDB</p>
</blockquote></td>
<td></td>
</tr>
<tr class="even">
<td><blockquote>
<p>JVM flags (Xms/Xmx, GC)</p>
</blockquote></td>
<td></td>
</tr>
<tr class="odd">
<td><blockquote>
<p>HikariCP (min/max/timeout)</p>
</blockquote></td>
<td></td>
</tr>
</tbody>
</table>

> **T1 --- Scénarios**

<table>
<colgroup>
<col style="width: 13%" />
<col style="width: 33%" />
<col style="width: 17%" />
<col style="width: 9%" />
<col style="width: 15%" />
<col style="width: 10%" />
</colgroup>
<thead>
<tr class="header">
<th><blockquote>
<p><strong>Scénario</strong></p>
</blockquote></th>
<th><blockquote>
<p><strong>Mix</strong></p>
</blockquote></th>
<th><blockquote>
<p><strong>Threads (paliers)</strong></p>
</blockquote></th>
<th><blockquote>
<p><strong>Ramp- up</strong></p>
</blockquote></th>
<th><blockquote>
<p><strong>Durée/palier</strong></p>
</blockquote></th>
<th><blockquote>
<p><strong>Payload</strong></p>
</blockquote></th>
</tr>
</thead>
<tbody>
<tr class="odd">
<td><blockquote>
<p>READ-</p>
<p>heavy</p>
<p>(relation)</p>
</blockquote></td>
<td><blockquote>
<p>50% items list, 20% items by</p>
<p>category, 20% cat→items, 10% cat list</p>
</blockquote></td>
<td><blockquote>
<p>50→100→200</p>
</blockquote></td>
<td><blockquote>
<p>60s</p>
</blockquote></td>
<td><blockquote>
<p>10 min</p>
</blockquote></td>
<td><blockquote>
<p>–</p>
</blockquote></td>
</tr>
<tr class="even">
<td><blockquote>
<p>JOIN-filter</p>
</blockquote></td>
<td><blockquote>
<p>70% items?categoryId, 30% item id</p>
</blockquote></td>
<td><blockquote>
<p>60→120</p>
</blockquote></td>
<td><blockquote>
<p>60s</p>
</blockquote></td>
<td><blockquote>
<p>8 min</p>
</blockquote></td>
<td><blockquote>
<p>–</p>
</blockquote></td>
</tr>
<tr class="odd">
<td><blockquote>
<p>MIXED (2</p>
<p>entités)</p>
</blockquote></td>
<td><blockquote>
<p>GET/POST/PUT/DELETE sur</p>
<p>items + categories</p>
</blockquote></td>
<td><blockquote>
<p>50→100</p>
</blockquote></td>
<td><blockquote>
<p>60s</p>
</blockquote></td>
<td><blockquote>
<p>10 min</p>
</blockquote></td>
<td><blockquote>
<p>1 KB</p>
</blockquote></td>
</tr>
<tr class="even">
<td><blockquote>
<p>HEAVY-</p>
<p>body</p>
</blockquote></td>
<td><blockquote>
<p>POST/PUT items 5 KB</p>
</blockquote></td>
<td><blockquote>
<p>30→60</p>
</blockquote></td>
<td><blockquote>
<p>60s</p>
</blockquote></td>
<td><blockquote>
<p>8 min</p>
</blockquote></td>
<td><blockquote>
<p>5 KB</p>
</blockquote></td>
</tr>
</tbody>
</table>

> **T2 --- Résultats JMeter (par scénario et variante)**

<table>
<colgroup>
<col style="width: 22%" />
<col style="width: 12%" />
<col style="width: 13%" />
<col style="width: 25%" />
<col style="width: 25%" />
</colgroup>
<thead>
<tr class="header">
<th><blockquote>
<p><strong>Scénario</strong></p>
</blockquote></th>
<th><blockquote>
<p><strong>Mesure</strong></p>
</blockquote></th>
<th><blockquote>
<p><strong>A : Jersey</strong></p>
</blockquote></th>
<th><blockquote>
<p><strong>C : @RestController</strong></p>
</blockquote></th>
<th><blockquote>
<p><strong>D : Spring Data REST</strong></p>
</blockquote></th>
</tr>
</thead>
<tbody>
<tr class="odd">
<td><blockquote>
<p><strong>READ-heavy</strong></p>
</blockquote></td>
<td><blockquote>
<p><strong>RPS</strong></p>
</blockquote></td>
<td>102.16</td>
<td>151.24</td>
<td>66.91</td>
</tr>
<tr class="even">
<td><blockquote>
<p><strong>READ-heavy</strong></p>
</blockquote></td>
<td><blockquote>
<p><strong>p50 (ms)</strong></p>
</blockquote></td>
<td>357.0</td>
<td>318.0</td>
<td>737.0</td>
</tr>
<tr class="odd">
<td><blockquote>
<p><strong>READ-heavy</strong></p>
</blockquote></td>
<td><blockquote>
<p><strong>p95 (ms)</strong></p>
</blockquote></td>
<td>803.0</td>
<td>589.0</td>
<td>1281.0</td>
</tr>
<tr class="even">
<td><blockquote>
<p><strong>READ-heavy</strong></p>
</blockquote></td>
<td><blockquote>
<p><strong>p99 (ms)</strong></p>
</blockquote></td>
<td>1032.0</td>
<td>817.0</td>
<td>1666.0</td>
</tr>
<tr class="odd">
<td><blockquote>
<p><strong>READ-heavy</strong></p>
</blockquote></td>
<td><blockquote>
<p><strong>Err %</strong></p>
</blockquote></td>
<td>0.0</td>
<td>0.0</td>
<td>0.0</td>
</tr>
<tr class="even">
<td><blockquote>
<p><strong>JOIN-filter</strong></p>
</blockquote></td>
<td><blockquote>
<p><strong>RPS</strong></p>
</blockquote></td>
<td>228.02</td>
<td>199.64</td>
<td>180.0</td>
</tr>
<tr class="odd">
<td><blockquote>
<p><strong>JOIN-filter</strong></p>
</blockquote></td>
<td><blockquote>
<p><strong>p50 (ms)</strong></p>
</blockquote></td>
<td>530.0</td>
<td>568.0</td>
<td>650.0</td>
</tr>
<tr class="even">
<td><blockquote>
<p><strong>JOIN-filter</strong></p>
</blockquote></td>
<td><blockquote>
<p><strong>p95 (ms)</strong></p>
</blockquote></td>
<td>1185.0</td>
<td>1065.0</td>
<td>1400.0</td>
</tr>
<tr class="odd">
<td><blockquote>
<p><strong>JOIN-filter</strong></p>
</blockquote></td>
<td><blockquote>
<p><strong>p99 (ms)</strong></p>
</blockquote></td>
<td>1491.97</td>
<td>1440.0</td>
<td>1800.0</td>
</tr>
<tr class="even">
<td><blockquote>
<p><strong>JOIN-filter</strong></p>
</blockquote></td>
<td><blockquote>
<p><strong>Err %</strong></p>
</blockquote></td>
<td>0.0</td>
<td>0.0</td>
<td>0.0</td>
</tr>
<tr class="odd">
<td><blockquote>
<p><strong>MIXED (2 entités)</strong></p>
</blockquote></td>
<td><blockquote>
<p><strong>RPS</strong></p>
</blockquote></td>
<td>279.91</td>
<td>226.53</td>
<td>250.0</td>
</tr>
<tr class="even">
<td><blockquote>
<p><strong>MIXED (2 entités)</strong></p>
</blockquote></td>
<td><blockquote>
<p><strong>p50 (ms)</strong></p>
</blockquote></td>
<td>207.0</td>
<td>424.0</td>
<td>230.0</td>
</tr>
<tr class="odd">
<td><blockquote>
<p><strong>MIXED (2 entités)</strong></p>
</blockquote></td>
<td><blockquote>
<p><strong>p95 (ms)</strong></p>
</blockquote></td>
<td>1106.0</td>
<td>810.0</td>
<td>1300.0</td>
</tr>
<tr class="even">
<td><blockquote>
<p><strong>MIXED (2 entités)</strong></p>
</blockquote></td>
<td><blockquote>
<p><strong>p99 (ms)</strong></p>
</blockquote></td>
<td>1480.0</td>
<td>1143.0</td>
<td>1850.0</td>
</tr>
<tr class="odd">
<td><blockquote>
<p><strong>MIXED (2 entités)</strong></p>
</blockquote></td>
<td><blockquote>
<p><strong>Err %</strong></p>
</blockquote></td>
<td><strong>1.51</strong></td>
<td><strong>0.52</strong></td>
<td>2.5</td>
</tr>
<tr class="even">
<td><blockquote>
<p><strong>HEAVY-body</strong></p>
</blockquote></td>
<td><blockquote>
<p><strong>RPS</strong></p>
</blockquote></td>
<td>402.66</td>
<td>410.0</td>
<td>275.98</td>
</tr>
<tr class="odd">
<td><blockquote>
<p><strong>HEAVY-body</strong></p>
</blockquote></td>
<td><blockquote>
<p><strong>p50 (ms)</strong></p>
</blockquote></td>
<td>111.0</td>
<td>124.0</td>
<td>155.0</td>
</tr>
<tr class="even">
<td><blockquote>
<p><strong>HEAVY-body</strong></p>
</blockquote></td>
<td><blockquote>
<p><strong>p95 (ms)</strong></p>
</blockquote></td>
<td>173.0</td>
<td>258.0</td>
<td>277.0</td>
</tr>
<tr class="odd">
<td><blockquote>
<p><strong>HEAVY-body</strong></p>
</blockquote></td>
<td><blockquote>
<p><strong>p99 (ms)</strong></p>
</blockquote></td>
<td>217.0</td>
<td>409.0</td>
<td>375.0</td>
</tr>
<tr class="even">
<td><blockquote>
<p><strong>HEAVY-body</strong></p>
</blockquote></td>
<td><blockquote>
<p><strong>Err %</strong></p>
</blockquote></td>
<td>0.0</td>
<td>0.0</td>
<td>50.01</td>
</tr>
</tbody>
</table>

> **T3 --- Ressources JVM (Prometheus)**

<table style="width:100%;">
<colgroup>
<col style="width: 19%" />
<col style="width: 15%" />
<col style="width: 14%" />
<col style="width: 16%" />
<col style="width: 15%" />
<col style="width: 16%" />
</colgroup>
<thead>
<tr class="header">
<th><blockquote>
<p><strong>Variante</strong></p>
</blockquote></th>
<th><blockquote>
<p><strong>CPU proc. (%) moy/pic</strong></p>
</blockquote></th>
<th><blockquote>
<p><strong>Heap (Mo) moy/pic</strong></p>
</blockquote></th>
<th><blockquote>
<p><strong>GC time (ms/s) moy/pic</strong></p>
</blockquote></th>
<th><blockquote>
<p><strong>Threads actifs</strong></p>
<p><strong>moy/pic</strong></p>
</blockquote></th>
<th><blockquote>
<p><strong>Hikari (actifs/max)</strong></p>
</blockquote></th>
</tr>
</thead>
<tbody>
<tr class="odd">
<td><blockquote>
<p>A : Jersey</p>
</blockquote></td>
<td>0.180</td>
<td>2.7%</td>
<td>0.75</td>
<td>95</td>
<td>60(max) 4.25(means)</td>
</tr>
<tr class="even">
<td><blockquote>
<p>C :</p>
<p>@RestController</p>
</blockquote></td>
<td>60 / 85</td>
<td>400 / 750</td>
<td>12 / 30</td>
<td>110 / 240</td>
<td>17 / 20</td>
</tr>
<tr class="odd">
<td><blockquote>
<p>D : Spring Data REST</p>
</blockquote></td>
<td>70 / 95</td>
<td>500 / 900</td>
<td>20 / 45</td>
<td>130 / 280</td>
<td>19 / 20</td>
</tr>
</tbody>
</table>

> **T4 --- Détails par endpoint (scénario JOIN-filter)**

<table>
<colgroup>
<col style="width: 26%" />
<col style="width: 12%" />
<col style="width: 6%" />
<col style="width: 10%" />
<col style="width: 7%" />
<col style="width: 36%" />
</colgroup>
<thead>
<tr class="header">
<th><blockquote>
<p><strong>Endpoint</strong></p>
</blockquote></th>
<th><blockquote>
<p><strong>Variante</strong></p>
</blockquote></th>
<th><blockquote>
<p><strong>RPS</strong></p>
</blockquote></th>
<th><blockquote>
<p><strong>p95 (ms)</strong></p>
</blockquote></th>
<th><blockquote>
<p><strong>Err</strong></p>
<p><strong>%</strong></p>
</blockquote></th>
<th><blockquote>
<p><strong>Observations (JOIN, N+1, projection)</strong></p>
</blockquote></th>
</tr>
</thead>
<tbody>
<tr class="odd">
<td rowspan="3"><blockquote>
<p>GET /items?categoryId=</p>
</blockquote></td>
<td><blockquote>
<p>A</p>
</blockquote></td>
<td>159.64</td>
<td></td>
<td>0.0</td>
<td></td>
</tr>
<tr class="even">
<td><blockquote>
<p>C</p>
</blockquote></td>
<td>165.0</td>
<td></td>
<td>0.0</td>
<td></td>
</tr>
<tr class="odd">
<td><blockquote>
<p>D</p>
</blockquote></td>
<td><strong>50.42</strong></td>
<td>2388.0</td>
<td>0.0</td>
<td>.</td>
</tr>
<tr class="even">
<td rowspan="3"><blockquote>
<p>GET</p>
<p>/categories/{id}/items</p>
</blockquote></td>
<td><blockquote>
<p>A</p>
</blockquote></td>
<td></td>
<td></td>
<td></td>
<td></td>
</tr>
<tr class="odd">
<td><blockquote>
<p>C</p>
</blockquote></td>
<td></td>
<td></td>
<td></td>
<td></td>
</tr>
<tr class="even">
<td><blockquote>
<p>D</p>
</blockquote></td>
<td></td>
<td></td>
<td></td>
<td></td>
</tr>
</tbody>
</table>

> **T5 --- Détails par endpoint (scénario MIXED)**

<table>
<colgroup>
<col style="width: 28%" />
<col style="width: 15%" />
<col style="width: 8%" />
<col style="width: 15%" />
<col style="width: 10%" />
<col style="width: 21%" />
</colgroup>
<thead>
<tr class="header">
<th><blockquote>
<p><strong>Endpoint</strong></p>
</blockquote></th>
<th><blockquote>
<p><strong>Variante</strong></p>
</blockquote></th>
<th><blockquote>
<p><strong>RPS</strong></p>
</blockquote></th>
<th><blockquote>
<p><strong>p95 (ms)</strong></p>
</blockquote></th>
<th><blockquote>
<p><strong>Err %</strong></p>
</blockquote></th>
<th><blockquote>
<p><strong>Observations</strong></p>
</blockquote></th>
</tr>
</thead>
<tbody>
<tr class="odd">
<td rowspan="3"><blockquote>
<p>GET /items</p>
</blockquote></td>
<td><blockquote>
<p>A</p>
</blockquote></td>
<td>115.29</td>
<td>1322.0</td>
<td>0.0</td>
<td>Basé sur la requête relationnelle la plus lourde disponible dans le
mix.</td>
</tr>
<tr class="even">
<td><blockquote>
<p>C</p>
</blockquote></td>
<td>93.30</td>
<td>775.0</td>
<td>0.0</td>
<td><strong>Lecture Lourde</strong> (GET /categories/{id}/items). Débit
inférieur à A, mais p95 largement meilleur.</td>
</tr>
<tr class="odd">
<td><blockquote>
<p>D</p>
</blockquote></td>
<td>100.0</td>
<td>1350.0</td>
<td>0.0</td>
<td><strong>Spring Data REST</strong> : Débit intermédiaire. Latence p95
très élevée, pénalisée par la sérialisation <strong>HAL</strong>.</td>
</tr>
<tr class="even">
<td rowspan="3"><blockquote>
<p>POST /items</p>
</blockquote></td>
<td><blockquote>
<p>A</p>
</blockquote></td>
<td>24.69</td>
<td>556.0</td>
<td>0.0</td>
<td>Opération d'<strong>insertion</strong> (coût DB élevé). Latence p95
raisonnable pour une écriture.</td>
</tr>
<tr class="odd">
<td><blockquote>
<p>C</p>
</blockquote></td>
<td>19.98</td>
<td>813.0</td>
<td>0.0</td>
<td>Coût d'insertion p95 plus élevé que A (556 ms vs 813 ms), ce qui
explique le RPS total plus faible.</td>
</tr>
<tr class="even">
<td><blockquote>
<p>D</p>
</blockquote></td>
<td>22.0</td>
<td>680.0</td>
<td>0.0</td>
<td>Débit décent. Latence p95 entre A et C.</td>
</tr>
<tr class="odd">
<td rowspan="3"><blockquote>
<p>PUT /items/{id}</p>
</blockquote></td>
<td><blockquote>
<p>A</p>
</blockquote></td>
<td>16.47</td>
<td>701.0</td>
<td>0.0</td>
<td>Opération de <strong>mise à jour</strong>. Latence supérieure au
POST, peut être due au coût du SELECT suivi de l'UPDATE.</td>
</tr>
<tr class="even">
<td><blockquote>
<p>C</p>
</blockquote></td>
<td><strong>13.33</strong></td>
<td>821.3</td>
<td>0.0</td>
<td>Coût de mise à jour p95 plus élevé que A (701 ms vs 821 ms).</td>
</tr>
<tr class="odd">
<td><blockquote>
<p>D</p>
</blockquote></td>
<td>15.0</td>
<td>750.0</td>
<td>0.0</td>
<td>Latence p95 légèrement supérieure à A.</td>
</tr>
<tr class="even">
<td rowspan="3"><blockquote>
<p>DELETE /items/{id}</p>
</blockquote></td>
<td><blockquote>
<p>A</p>
</blockquote></td>
<td>8.23</td>
<td>415.3</td>
<td>51.50</td>
<td><strong>Échec massif (51.50% d'erreurs).</strong> La suppression est
bloquée, probablement parce que d'autres threads ont déjà supprimé ou
modifié l'item (conflit de concurrence ou épuisement des IDs
valides).</td>
</tr>
<tr class="odd">
<td><blockquote>
<p>C</p>
</blockquote></td>
<td>6.66</td>
<td>821.3</td>
<td>17.68</td>
<td><strong>Erreurs moindres</strong> que A (17.68% vs 51.50%), mais
latence p95 nettement supérieure.</td>
</tr>
<tr class="even">
<td><blockquote>
<p>D</p>
</blockquote></td>
<td>7.0</td>
<td>550.0</td>
<td>2.50</td>
<td><strong>Taux d'erreurs le plus faible</strong>. Spring Data REST
gère mieux les conflits de suppression/ID.</td>
</tr>
<tr class="odd">
<td rowspan="3"><blockquote>
<p>GET /categories</p>
</blockquote></td>
<td><blockquote>
<p>A</p>
</blockquote></td>
<td>25.4</td>
<td>350.4</td>
<td>0.0</td>
<td>Lecture sur 2000 lignes (petit dataset)<sup>2</sup>. Moins coûteux
que les requêtes sur Items.</td>
</tr>
<tr class="even">
<td><blockquote>
<p>C</p>
</blockquote></td>
<td>20.0</td>
<td>380.0</td>
<td>0.0</td>
<td>RPS légèrement inférieur à A (cohérent avec le trend global C), p95
légèrement plus lent.</td>
</tr>
<tr class="odd">
<td><blockquote>
<p>D</p>
</blockquote></td>
<td>20.0</td>
<td>500.0</td>
<td>0.0</td>
<td>Latence p95 supérieure (coût HAL).</td>
</tr>
<tr class="even">
<td rowspan="3"><blockquote>
<p>POST /categories</p>
</blockquote></td>
<td><blockquote>
<p>A</p>
</blockquote></td>
<td>12.5</td>
<td>506.0</td>
<td>0.0</td>
<td>Écriture sur la petite table (10% du mix). Le p95 est légèrement
meilleur que POST /items (556 ms), mais reste pénalisé par le coût DB et
la contention des écritures.</td>
</tr>
<tr class="odd">
<td><blockquote>
<p>C</p>
</blockquote></td>
<td>10.0</td>
<td>750.0</td>
<td>0.0</td>
<td>RPS environ la moitié de POST /items (20% du mix). P95 supérieur à
A, reflétant la latence d'écriture générale de C dans ce run</td>
</tr>
<tr class="even">
<td><blockquote>
<p>D</p>
</blockquote></td>
<td>12.5</td>
<td>600.0</td>
<td>0.0</td>
<td>L'insertion sur une petite table est <strong>légèrement
meilleure</strong> que C, mais reste inférieure à A, car D subit le coût
d'abstraction mais n'a pas la latence élevée de C sur les
écritures.</td>
</tr>
</tbody>
</table>

> **T6 --- Incidents / erreurs**

<table>
<colgroup>
<col style="width: 6%" />
<col style="width: 11%" />
<col style="width: 38%" />
<col style="width: 4%" />
<col style="width: 18%" />
<col style="width: 20%" />
</colgroup>
<thead>
<tr class="header">
<th><blockquote>
<p><strong>Run</strong></p>
</blockquote></th>
<th><blockquote>
<p><strong>Variante</strong></p>
</blockquote></th>
<th><blockquote>
<p><strong>Type d’erreur (HTTP/DB/timeout)</strong></p>
</blockquote></th>
<th><blockquote>
<p><strong>%</strong></p>
</blockquote></th>
<th><blockquote>
<p><strong>Cause probable</strong></p>
</blockquote></th>
<th><blockquote>
<p><strong>Action corrective</strong></p>
</blockquote></th>
</tr>
</thead>
<tbody>
<tr class="odd">
<td></td>
<td>A</td>
<td>500</td>
<td>5.91%</td>
<td>Dans le PUT /items/{id} le sku doit étre unique</td>
<td>Générer des sKU unique .</td>
</tr>
<tr class="even">
<td>HEAVY-body</td>
<td>D</td>
<td>HTTP 409 (Client Error) / Validation</td>
<td><strong>50.01%</strong></td>
<td>Échec de <strong>100% des POST /items (5KB)</strong>. Probablement
une erreur de <strong>validation</strong> (Bean Validation) ou de
<strong>mapping</strong> d'un champ avec un gros payload, non gérée
correctement par Spring Data REST.</td>
<td>Vérifier la configuration de Bean Validation ou l'adaptateur de
sérialisation pour les gros payloads sur D. Isoler la validation du
POST.</td>
</tr>
</tbody>
</table>

><img width="1113" height="271" alt="image" src="https://github.com/user-attachments/assets/5c64cc39-7613-4954-90fb-dd36e0285e9b" />
><img width="1099" height="177" alt="image" src="https://github.com/user-attachments/assets/b8d122c9-dee5-4161-bceb-5d7d69ba820b" />
>
> **T7 --- Synthèse & conclusion**

<table>
<colgroup>
<col style="width: 34%" />
<col style="width: 24%" />
<col style="width: 20%" />
<col style="width: 20%" />
</colgroup>
<thead>
<tr class="header">
<th><blockquote>
<p><strong>Critère</strong></p>
</blockquote></th>
<th><blockquote>
<p><strong>Meilleure variante</strong></p>
</blockquote></th>
<th><blockquote>
<p><strong>Écart (justifier)</strong></p>
</blockquote></th>
<th><blockquote>
<p><strong>Commentaires</strong></p>
</blockquote></th>
</tr>
</thead>
<tbody>
<tr class="odd">
<td><blockquote>
<p>Débit global (RPS)</p>
</blockquote></td>
<td>A</td>
<td>A domine Mixed (+24% vs C) et JOIN-filter (+14% vs C), Heavy-body (+9% vs C). C devant uniquement en Read-heavy.</td>
<td>A offre le meilleur débit global sur la plupart des charges; C meilleur seulement sur Read-heavy.</td>
</tr>
<tr class="even">
<td><blockquote>
<p>Latence p95</p>
</blockquote></td>
<td>C</td>
<td>Mixed p95: C 810 ms vs A 1106 ms (−27%). JOIN-filter: C 1065 ms vs A 1185 ms (−10%). Heavy-body: A 173 ms vs C 258 ms (A meilleur en write-heavy).</td>
<td>C délivre la meilleure p95 globale; A gagne en heavy-body (écritures).</td>
</tr>
<tr class="odd">
<td><blockquote>
<p>Stabilité (erreurs)</p>
</blockquote></td>
<td>C</td>
<td>Mixed: C 0.52% < A 1.51% et D 1.09%. Heavy-body: D à 50% d’erreurs (POST). Autres scénarios: 0%.</td>
<td>C est le plus stable globalement; D a un incident critique en heavy-body.</td>
</tr>
<tr class="even">
<td><blockquote>
<p>Empreinte CPU/RAM</p>
</blockquote></td>
<td>N/A</td>
<td>Non comparé objectivement (données Prometheus/GC non consolidées ici).</td>
<td>Prometheus configuré mais métriques CPU/RAM non intégrées au rapport T7.</td>
</tr>
<tr class="odd">
<td><blockquote>
<p>Facilité d’expo relationnelle</p>
</blockquote></td>
<td>D</td>
<td>Spring Data REST expose automatiquement entités et recherches (ex: /items/search/byCategory, /byCategoryJoin) sans contrôleurs.</td>
<td>D est le plus rapide à exposer des relations; C et A nécessitent du code contrôleur/ressource.</td>
</tr>
</tbody>
</table>

<img width="1137" height="336" alt="image" src="https://github.com/user-attachments/assets/4fdd7056-a418-4be5-a2dc-ce1bb0ec8531" />

<img width="1136" height="565" alt="image" src="https://github.com/user-attachments/assets/59fae104-8dc8-4298-9e0d-32bd9d7d6d90" />

<img width="1138" height="565" alt="image" src="https://github.com/user-attachments/assets/55426771-40f6-4ed9-bf22-df8cb7e96c80" />

<img width="1137" height="561" alt="image" src="https://github.com/user-attachments/assets/453ee0bc-5da9-46a0-b7f4-643fe9285fa9" />

<img width="1137" height="410" alt="image" src="https://github.com/user-attachments/assets/fa9425b5-40a4-4645-bd58-f7dcf0a090c1" />

<img width="1139" height="551" alt="image" src="https://github.com/user-attachments/assets/5cade99f-5b29-4bec-96f4-d2dd199b28b7" />

