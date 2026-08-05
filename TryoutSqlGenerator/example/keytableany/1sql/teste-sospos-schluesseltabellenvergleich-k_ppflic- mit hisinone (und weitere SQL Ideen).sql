



-------------------
select * from pg_available_extensions;

--############################################################################
--Teste sospos schluesseltabellen im Vergleich mit HISinOne Schlüsseltabellen
--############################################################################
--Merke: Jede Anweisung separat ausführen. 
--       Gleichzeitig ausgeführt gibt es folgende Fehlermeldung:
--       FEHLER:  Funktion »dblink_connect« existiert bereits mit den selben Argumenttypen
--
--       Bzw. das Beenden der Verbindung überschreibt die Ausgabe mit "OK"


--Erstelle die Extension, falls noch nicht geschehen
create extension if not exists dblink;

--Erstelle die Verbindung zur Datenbank
--Merke: Solch eine Verbindung darf nur 1x existieren
--       Daher unbedingt mit dem Schema vorangestellt arbeiten
SELECT public.dblink_connect('FGLconnection6', 'dbname=h1_tubaf_202412_20250625 host=127.0.0.1 port=5433 user=postgres password=postgres');

-- Beispiel für dblink_exec() mit dem alternativ direkt auf der Datenbank etwas gemacht werden kann:
-- SELECT dblink_exec('myconn', 'CREATE TABLE neue_tabelle AS SELECT * FROM vorhandene_tabelle;');

-- Beispiel für dblink():
SELECT id, uniquename, hiskey_id, defaulttext FROM dblink('FGLconnection6', 'SELECT id, uniquename, hiskey_id, defaulttext FROM hisinone.k_compulsory') AS t1(id text, uniquename text, hiskey_id text, defaulttext text);

-- Baue die Verbindung wieder ab.
SELECT public.dblink_disconnect('FGLconnection6');

--------------------------------------------------------------------------------------------
-- Prüfe nun die SOSPOS Tabelle
select * from k_ppflic;


----------------------------------------------------------------------------------------------------
--###########################################################################
--START: Kombiniere beides und bei Unterschied diesen Datensatz markieren
--############################################################################
--Erstelle die Extension, falls noch nicht geschehen
create extension if not exists dblink;

--Erstelle die Verbindung zur Datenbank
--Merke: Solch eine Verbindung darf nur 1x existieren
--       Daher unbedingt mit dem Schema vorangestellt arbeiten
SELECT public.dblink_connect('FGLconnection6', 'dbname=h1_tubaf_202412_20250625 host=127.0.0.1 port=5433 user=postgres password=postgres');

-- Beispiel für dblink_exec() mit dem alternativ direkt auf der Datenbank etwas gemacht werden kann:
-- SELECT dblink_exec('myconn', 'CREATE TABLE neue_tabelle AS SELECT * FROM vorhandene_tabelle;');

-- Beispiel für dblink():
--SELECT id, uniquename FROM dblink('FGLconnection6', 'SELECT id, uniquename FROM hisinone.k_compulsory') AS t1(id text, uniquename text);

--Merke: Hierzu müsste eine View aufgebaut werden tubaf.aktive_pord_filter
--       select distinct ppflicht, count(*) from pord natural join tubaf.aktive_pord_filter group by 1;

select 
CASE
  WHEN t1.uniquename != ppflicht then '!'
 ELSE
  ''
 END AS abweichung,
 ppflicht, t1.uniquename, t1.hiskey_id, t1.defaulttext
 
FROM k_ppflic
RIGHT JOIN dblink('FGLconnection6', 'SELECT id, uniquename, hiskey_id, defaulttext FROM hisinone.k_compulsory') AS t1(id text, uniquename text, hiskey_id text, defaulttext text) on ppflicht = t1.uniquename;

-- Baue die Verbindung wieder ab.
SELECT public.dblink_disconnect('FGLconnection6');
--###########################################################################
--ENDE: Kombiniere beides und bei Unterschied diesen Datensatz markieren
--############################################################################

--###########################################################################
--START: Kombiniere beides und bei Unterschied diesen Datensatz markieren
--       AUF UNTERSCHIEDLICHEN SERVERN (GEHT ABER PRAKTISCH HIER BEI DER TUBAF NICHT)
--############################################################################
--Erstelle die Extension, falls noch nicht geschehen
create extension if not exists dblink;

--Erstelle die Verbindung zur Datenbank
--Merke: Solch eine Verbindung darf nur 1x existieren
--       Daher unbedingt mit dem Schema vorangestellt arbeiten
--Das geht nicht, da der Server nur über PROXY erreicht werden kann. SELECT public.dblink_connect('FGLconnection6', 'dbname=hisinone_cust host=db-cust-01 port=5432 user=fl86kyvo password=fcku');
SELECT public.dblink_connect('FGLconnection7', 'dbname=h1_tubaf_202412_20250701 host=127.0.0.1 port=5433 user=postgres password=postgres');

-- Beispiel für dblink_exec() mit dem alternativ direkt auf der Datenbank etwas gemacht werden kann:
-- SELECT dblink_exec('myconn', 'CREATE TABLE neue_tabelle AS SELECT * FROM vorhandene_tabelle;');

-- Beispiel für dblink():
--SELECT id, uniquename FROM dblink('FGLconnection7', 'SELECT id, uniquename FROM hisinone.k_compulsory') AS t1(id text, uniquename text);

--Merke: Hier bei der Abweichungsprüfung einen Trick mit COALESCE anwenden, der null - Werte dann auch berücksichtigt
select 
CASE
  WHEN COALESCE(t1.uniquename,'') != COALESCE(ppflicht,'') then '!'
 ELSE
  ''
 END AS abweichung,
 ppflicht, dtxt, t1.uniquename, t1.defaulttext, t1.hiskey_id
 
FROM k_ppflic
--blende die HIO Datensätze ohne "Gegenstück" aus LEFT JOIN dblink('FGLconnection7', 'SELECT id, uniquename, hiskey_id, defaulttext FROM hisinone.k_compulsory') AS t1(id text, uniquename text, hiskey_id text, defaulttext text) on ppflicht = t1.uniquename;
--blende die SOSPOS Datensätze ohne "Gegenstück" aus RIGHT JOIN dblink('FGLconnection7', 'SELECT id, uniquename, hiskey_id, defaulttext FROM hisinone.k_compulsory') AS t1(id text, uniquename text, hiskey_id text, defaulttext text) on ppflicht = t1.uniquename;
--zeige nur "Gemeinsamen" Datensätze JOIN dblink('FGLconnection7', 'SELECT id, uniquename, hiskey_id, defaulttext FROM hisinone.k_compulsory') AS t1(id text, uniquename text, hiskey_id text, defaulttext text) on ppflicht = t1.uniquename;
--s. Left join .... LEFT OUTER JOIN dblink('FGLconnection7', 'SELECT id, uniquename, hiskey_id, defaulttext FROM hisinone.k_compulsory') AS t1(id text, uniquename text, hiskey_id text, defaulttext text) on ppflicht = t1.uniquename;
--s. Right join RIGHT OUTER JOIN dblink('FGLconnection7', 'SELECT id, uniquename, hiskey_id, defaulttext FROM hisinone.k_compulsory') AS t1(id text, uniquename text, hiskey_id text, defaulttext text) on ppflicht = t1.uniquename;
--Alles
FULL OUTER JOIN dblink('FGLconnection7', 'SELECT id, uniquename, hiskey_id, defaulttext FROM hisinone.k_compulsory') AS t1(id text, uniquename text, hiskey_id text, defaulttext text) on ppflicht = t1.uniquename;


-- Baue die Verbindung wieder ab.
SELECT public.dblink_disconnect('FGLconnection7');
--###########################################################################
--ENDE: Kombiniere beides und bei Unterschied diesen Datensatz markieren
--############################################################################

--Konsequenzen: Aktualisierungen in HISinOne
---- Merke: Daten in der externen Datenbank aktualisieren
---- SELECT dblink_exec('myconn', 'UPDATE meine_tabelle SET spalte1 = ''neuer_wert'' WHERE bedingung;');
SELECT dblink_exec('FGLconnection7', 'UPDATE hisinone.k_compulsory SET uniquename = ''ZF'' WHERE uniquename IN (''W'')');
--TODO nach der Migration: hiskey_id für neue Datensätze mit uniquename: WG, GV, FL  ABER WIE?

-- Baue die Verbindung wieder ab.
SELECT public.dblink_disconnect('FGLconnection7');








--#######################################################################
--#######################################################################
--IDEE: WERT ÜEBERNEHMEN
UPDATE my_table t 
SET  column_name=i.column_name, 
FROM  
dblink('dbname=other_db','SELECT id, x_column_name FROM my_table') as i(id INT, column_name TEXT) where t.id=i.id;

INSERT INTO table_name [ AS alias ] [ ( column_name [, ...] ) ]
    [ OVERRIDING { SYSTEM | USER } VALUE ]
    { DEFAULT VALUES | VALUES ( { expression | DEFAULT } [, ...] ) [, ...] | query }
    [ ON CONFLICT [ conflict_target ] conflict_action ]
    [ RETURNING { * | output_expression [ [ AS ] output_name ] } [, ...] ]


--################################################################################
--################################################################################
--------------------------------------------------------------------------------------
--- IDEE:
--------------------------------------------------------
CREATE OR REPLACE FUNCTION db_link_function()
 RETURNS void AS
 $BODY$
 declare
  rec record;
 BEGIN

 BEGIN   
  PERFORM  dblink_connect('FGLconnect', 'hostaddr=127.0.0.1 port=5433 dbname=h1_tubaf_202412_20250625 user=postgres password=postgres');
  --the EXCEPTION
  EXCEPTION
   WHEN duplicate_object THEN --code error 42710
    RAISE NOTICE 'this connections exists';
 END;
 
  --select data via dblink
  SELECT id, uniquename FROM dblink('FGLconnect', 'SELECT id, uniquename FROM k_compulsory') AS t1(id text, uniquename text);
 --SELECT * FROM dblink('FGLconnect','SELECT * FROM k_compulsory') AS t into rec; --(cid int, cname text) into rec;
 --raise notice 'value of rec: %,%', rec.id, rec.name;  
-- raise notice 'value of rec: %,%', rec.id, rec.uniquename;  
END;
$BODY$
LANGUAGE plpgsql;



--########################################
CREATE OR REPLACE FUNCTION db_link_function()
 RETURNS void AS
 $BODY$
 declare
  rec record;
 BEGIN

 BEGIN   
  PERFORM  dblink_connect('dblinktest', 'hostaddr=127.0.0.1 port=5435 dbname=dell user=postgres password=password');
  --the EXCEPTION
  EXCEPTION
   WHEN duplicate_object THEN --code error 42710
    RAISE NOTICE 'this connections exists';

 END;
   --select data via dblink
 SELECT * FROM dblink('dblinktest','SELECT * FROM categories where category=1') AS t(cid int, cname text) into rec;
 raise notice 'value of rec: %,%', rec.cid, rec.cname;  


END;
$BODY$
LANGUAGE plpgsql;