--------------------------------------------------------------------------------------------
-- Prüfe nun die SOSPOS Tabelle, wg. der Feldnamen
select * from k_pform;

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
SELECT public.dblink_connect('FGLconnectionCust01', 'dbname=h1_tubaf_202412_20250701 host=127.0.0.1 port=5433 user=postgres password=postgres');

--VERGLEICH MIT MUSTERDATENBANK HISINONE
SELECT public.dblink_connect('FGLconnection202412', 'dbname=h1_muster_202412_20250219 host=127.0.0.1 port=5432 user=postgres password=postgres');

--VERGLEICHE (ALTE) CUST01 DB NACH INSERTS MIT MUSTERDATENBANK HISINONE NUR ALS NACHTEST
SELECT public.dblink_connect('FGLconnectionInsertReference', 'dbname=h1_tubaf_202312_20250424 host=127.0.0.1 port=5432 user=postgres password=postgres');

--#############
-- Vergleich HIO musterdb mit aktuellem HIO
select 
t1.uniquename, t1.defaulttext, t1.hiskey_id, 
CASE
  WHEN COALESCE(t1.uniquename,'') != COALESCE(t2.uniquename,'') then '!'
 ELSE
  ''
 END AS abweichung,
t2.uniquename, t2.defaulttext, t2.hiskey_id
 
FROM dblink('FGLconnection202412', 'SELECT id, uniquename, hiskey_id, defaulttext FROM hisinone.k_examform') AS t2(id text, uniquename text, hiskey_id text, defaulttext text) 
FULL OUTER JOIN dblink('FGLconnectionInsertReference', 'SELECT id, uniquename, hiskey_id, defaulttext FROM hisinone.k_examform') AS t1(id text, uniquename text, hiskey_id text, defaulttext text) on t2.uniquename = t1.uniquename;


--################
-- Vergleich SOSPOS mit aktuellem HIO
select 
CASE
  WHEN COALESCE(t1.uniquename,'') != COALESCE(pform,'') then '!'
 ELSE
  ''
 END AS abweichung,
 pform, dtxt, t1.uniquename, t1.defaulttext, t1.hiskey_id
 
FROM k_pform
FULL OUTER JOIN dblink('FGLconnectionInsertReference', 'SELECT id, uniquename, hiskey_id, defaulttext FROM hisinone.k_examform') AS t1(id text, uniquename text, hiskey_id text, defaulttext text) on pform = t1.uniquename;

--###########################################################################
--ENDE: Kombiniere beides und bei Unterschied diesen Datensatz markieren
--############################################################################

--Konsequenzen: Aktualisierungen in HISinOne
--SELECT dblink_exec('FGLconnection7', 'UPDATE hisinone.k_compulsory SET uniquename = ''ZF'' WHERE uniquename IN (''W'')');

-- Baue die Verbindung wieder ab.
SELECT public.dblink_disconnect('FGLconnectionCust01');
SELECT public.dblink_disconnect('FGLconnection202412');
SELECT public.dblink_disconnect('FGLconnectionInsertReference');


