--------------------------------------------------------------------------------------------
-- Prüfe nun die SOSPOS Tabelle, wg. der Feldnamen
select * from k_artzuord;

--###########################################################################
--START: Kombiniere beides und bei Unterschied diesen Datensatz markieren
--       THEORETISCH AUF UNTERSCHIEDLICHEN SERVERN (GEHT ABER PRAKTISCH HIER BEI DER TUBAF NICHT)
--############################################################################
-------------------
select * from pg_available_extensions;

--Erstelle die Extension, falls noch nicht geschehen
create extension if not exists dblink;

--Erstelle die Verbindung zur Datenbank
--Das geht nicht, da der Server nur über PROXY erreicht werden kann. SELECT public.dblink_connect('FGLconnection6', 'dbname=hisinone_cust host=db-cust-01 port=5432 user=fl86kyvo password=fcku');
SELECT public.dblink_connect('FGLconnection7', 'dbname=h1_tubaf_202412_20250701 host=127.0.0.1 port=5433 user=postgres password=postgres');

--VERGLEICH MIT MUSTERDATENBANK HISINONE
SELECT public.dblink_connect('FGLconnection202412', 'dbname=h1_muster_202412_20250219 host=127.0.0.1 port=5432 user=postgres password=postgres');

-- Vergleich SOSPOS mit aktuellem HIO
select 
CASE
  WHEN COALESCE(t1.uniquename,'') != COALESCE(artzuord,'') then '!'
 ELSE
  ''
 END AS abweichung,
 artzuord, dtxt, t1.uniquename, t1.defaulttext, t1.hiskey_id
 
FROM k_artzuord
FULL OUTER JOIN dblink('FGLconnection7', 'SELECT id, uniquename, hiskey_id, defaulttext FROM hisinone.k_unitrelationtype') AS t1(id text, uniquename text, hiskey_id text, defaulttext text) on artzuord = t1.uniquename;


-- Vergleich HIO musterdb mit aktuellem HIO
select 
t1.uniquename, t1.defaulttext, t1.hiskey_id, 
CASE
  WHEN COALESCE(t1.uniquename,'') != COALESCE(t2.uniquename,'') then '!'
 ELSE
  ''
 END AS abweichung,
t2.uniquename, t2.defaulttext, t2.hiskey_id
 
FROM dblink('FGLconnection202412', 'SELECT id, uniquename, hiskey_id, defaulttext FROM hisinone.k_unitrelationtype') AS t2(id text, uniquename text, hiskey_id text, defaulttext text) 
FULL OUTER JOIN dblink('FGLconnection7', 'SELECT id, uniquename, hiskey_id, defaulttext FROM hisinone.k_unitrelationtype') AS t1(id text, uniquename text, hiskey_id text, defaulttext text) on t2.uniquename = t1.uniquename;



--###########################################################################
--ENDE: Kombiniere beides und bei Unterschied diesen Datensatz markieren
--############################################################################

--Konsequenzen: Aktualisierungen in HISinOne
--SELECT dblink_exec('FGLconnection7', 'UPDATE hisinone.k_compulsory SET uniquename = ''ZF'' WHERE uniquename IN (''W'')');

-- Baue die Verbindung wieder ab.
SELECT public.dblink_disconnect('FGLconnection7');
SELECT public.dblink_disconnect('FGLconnection202412');
