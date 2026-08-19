SELECT ident.identnr AS identnr, identroll2.verbindung_integer AS pid, k_ppruef.enr AS pgd_join_id
				, ident.vorname AS firstname, ident.name as surname, ident.geschl as gender, ident.anti as title_academicdegree, ident.antizudtxt as nameprefix
				, ident.titel_nachgestellt AS namesuffix, identroll.rolle AS rolle
				, k_ppruef.gebdat AS birthdate
				, identzusatz.inhalt AS pgd_join_id2
			FROM k_ppruef 
     		INNER JOIN identroll ON (identroll.verbindung_char = k_ppruef.ppruefer AND identroll.rolle = 'P')
            INNER  JOIN ident ON (ident.identnr = identroll.identnr)
            LEFT OUTER JOIN identroll AS identroll2 ON (identroll.identnr = identroll2.identnr AND identroll2.rolle IN ('F')) 
            LEFT JOIN identzusatz ON (ident.identnr=identzusatz.identnr AND identzusatz.spaltenname='SVA')
            WHERE ident.name IS NOT NULL AND TRIM(ident.name) != '' 
            AND (k_ppruef.prgrup NOT IN ('O','M','U') OR k_ppruef.prgrup IS NULL)
               -- [pruefer_restriction]
			 AND ident.name = 'Rogler'
            ORDER BY ident.identnr

--++++++++++++++++
SELECT ident.identnr AS identnr, identroll2.verbindung_integer AS pid, k_ppruef.enr AS pgd_join_id
				, ident.vorname AS firstname, ident.name as surname, ident.geschl as gender, ident.anti as title_academicdegree, ident.antizudtxt as nameprefix
				, ident.titel_nachgestellt AS namesuffix, identroll.rolle AS rolle
				, k_ppruef.gebdat AS birthdate
				, identzusatz.inhalt AS pgd_join_id2
			FROM k_ppruef 
     		INNER JOIN identroll ON (identroll.verbindung_char = k_ppruef.ppruefer AND identroll.rolle = 'P')
            INNER  JOIN ident ON (ident.identnr = identroll.identnr)
            LEFT OUTER JOIN identroll AS identroll2 ON (identroll.identnr = identroll2.identnr AND identroll2.rolle IN ('F')) 
            LEFT JOIN identzusatz ON (ident.identnr=identzusatz.identnr AND identzusatz.spaltenname='SVA')
            WHERE ident.name IS NOT NULL AND TRIM(ident.name) != '' 
            AND (k_ppruef.prgrup NOT IN ('O','M','U') OR k_ppruef.prgrup IS NULL)
               -- [pruefer_restriction]
			 AND ident.name = 'Bongaerts'
            ORDER BY ident.identnr

--############################################


--############################################

select distinct anti from ident

select distinct k_ppruef.nachname, k_ppruef.vorname, ident.anti as title_academicdegree 
FROM k_ppruef 
     		INNER JOIN identroll ON (identroll.verbindung_char = k_ppruef.ppruefer AND identroll.rolle = 'P')
            INNER  JOIN ident ON (ident.identnr = identroll.identnr)
            LEFT OUTER JOIN identroll AS identroll2 ON (identroll.identnr = identroll2.identnr AND identroll2.rolle IN ('F')) 
            LEFT JOIN identzusatz ON (ident.identnr=identzusatz.identnr AND identzusatz.spaltenname='SVA')
            WHERE ident.name IS NOT NULL AND TRIM(ident.name) != '' 
            AND (k_ppruef.prgrup NOT IN ('O','M','U') OR k_ppruef.prgrup IS NULL)
               -- [pruefer_restriction]
			 --AND ident.name = 'Rogler'
             --ORDER BY ident.identnr
			 AND ident.anti is distinct from null

--###########################################
--Ersetze darin Dr. (ABER Nur ALS Teilstring, )
select distinct k_ppruef.nachname, k_ppruef.vorname, replace(ident.anti, 'Dr.','') as title_academicdegree 
FROM k_ppruef 
     		INNER JOIN identroll ON (identroll.verbindung_char = k_ppruef.ppruefer AND identroll.rolle = 'P')
            INNER  JOIN ident ON (ident.identnr = identroll.identnr)
            LEFT OUTER JOIN identroll AS identroll2 ON (identroll.identnr = identroll2.identnr AND identroll2.rolle IN ('F')) 
            LEFT JOIN identzusatz ON (ident.identnr=identzusatz.identnr AND identzusatz.spaltenname='SVA')
            WHERE ident.name IS NOT NULL AND TRIM(ident.name) != '' 
            AND (k_ppruef.prgrup NOT IN ('O','M','U') OR k_ppruef.prgrup IS NULL)
               -- [pruefer_restriction]
			 --AND ident.name = 'Rogler'
             --ORDER BY ident.identnr
			 AND ident.anti is distinct from null

--################################################
--Ersetze darin Dr. (WICHTIG, als Gesamtstring)
--Auch Dr.-Ing., das ist auch ein akademischer Grad
--Auch Dr. rer. nat. habil. ist ein akademischer Grad
--Auch PH.D.                    ist ein akademischer Grad
--auch Dipl.-Phys.              ist ein akademischer Grad
select distinct k_ppruef.nachname, k_ppruef.vorname, 
CASE
	WHEN ident.anti ='Dipl.-Phys.              ' THEN ''
	WHEN ident.anti ='Dipl.-Ing.               ' THEN ''	
	WHEN ident.anti ='Dr.' THEN ''
	WHEN ident.anti ='Dr,' THEN ''                     
	WHEN ident.anti ='Dr.-Ing' THEN ''
	WHEN ident.anti ='Dr.-Ing.                 ' THEN ''
	WHEN ident.anti ='Dr.phil.                 ' THEN ''	
	WHEN ident.anti ='Dr. rer. nat.            ' THEN ''
	WHEN ident.anti ='Dr.rer.nat.              ' THEN ''
	WHEN ident.anti ='PH.D.                    ' THEN ''		
	WHEN ident.anti ='Univ.Prof. Dr.' THEN 'Univ.Prof.'
	WHEN ident.anti ='Univ.-Prof. Dr.' THEN 'Univ.Prof.'
	WHEN ident.anti ='Prof. Dr.' THEN 'Prof.'
	WHEN ident.anti ='Prof. Dr.-Ing.           ' THEN 'Prof.'	
	WHEN ident.anti ='Priv-Doz. Dr.' THEN 'Priv.-Doz'
	WHEN ident.anti ='Priv.-Doz. Dr.           ' THEN 'Priv.-Doz'	
	WHEN ident.anti ='PD Dr. rer. nat. habil.  ' THEN 'Priv.-Doz'	
	WHEN ident.anti ='PD Dr.' THEN 'Priv.-Doz'
	WHEN ident.anti ='PD Dr.-Ing.habil.        ' THEN 'Priv.-Doz'
	WHEN ident.anti ='Gast-Prof. Dr.' THEN 'Gast-Prof.'
	WHEN ident.anti ='Jun.-Prof. Dr.' THEN 'Jun.-Prof.'
	WHEN ident.anti ='Prof. Dr. rer. nat.      ' THEN 'Prof.'
	WHEN ident.anti ='Prof.Dr.rer.nat.         ' THEN 'Prof.'
	WHEN ident.anti ='Prof.Dr. rer. nat. habil.' THEN 'Prof.'
	WHEN ident.anti ='Prof.Dr.rer.nat.habil.   ' THEN 'Prof.'
	
	
	ELSE ident.anti 
END as title_ohne_academic_degree 
FROM k_ppruef 
     		INNER JOIN identroll ON (identroll.verbindung_char = k_ppruef.ppruefer AND identroll.rolle = 'P')
            INNER  JOIN ident ON (ident.identnr = identroll.identnr)
            LEFT OUTER JOIN identroll AS identroll2 ON (identroll.identnr = identroll2.identnr AND identroll2.rolle IN ('F')) 
            LEFT JOIN identzusatz ON (ident.identnr=identzusatz.identnr AND identzusatz.spaltenname='SVA')
            WHERE ident.name IS NOT NULL AND TRIM(ident.name) != '' 
            AND (k_ppruef.prgrup NOT IN ('O','M','U') OR k_ppruef.prgrup IS NULL)
               -- [pruefer_restriction]
			 --AND ident.name = 'Rogler'
             --ORDER BY ident.identnr
			 AND ident.anti is distinct from null

--###########################
--übrigbleibende Titel (ECHTE Titel)
--Ersetze darin Dr. (WICHTIG, als Gesamtstring)
--Auch Dr.-Ing., das ist auch ein akademischer Grad
--Auch Dr. rer. nat. habil. ist ein akademischer Grad
--Auch PH.D.                    ist ein akademischer Grad
--auch Dipl.-Phys.              ist ein akademischer Grad
select distinct 
CASE
	WHEN ident.anti ='Dipl.-Phys.              ' THEN ''
	WHEN ident.anti ='Dipl.-Ing.               ' THEN ''	
	WHEN ident.anti ='Dr.' THEN ''
	WHEN ident.anti ='Dr,' THEN ''                     
	WHEN ident.anti ='Dr.-Ing' THEN ''
	WHEN ident.anti ='Dr.-Ing.                 ' THEN ''
	WHEN ident.anti ='Dr.phil.                 ' THEN ''	
	WHEN ident.anti ='Dr. rer. nat.            ' THEN ''
	WHEN ident.anti ='Dr.rer.nat.              ' THEN ''	
	WHEN ident.anti ='PH.D.                    ' THEN ''		
	WHEN ident.anti ='Univ.Prof. Dr.' THEN 'Univ.Prof.'
	WHEN ident.anti ='Univ.-Prof. Dr.' THEN 'Univ.Prof.'
	WHEN ident.anti ='Prof. Dr.' THEN 'Prof.'
	WHEN ident.anti ='Prof. Dr.-Ing.           ' THEN 'Prof.'	
	WHEN ident.anti ='Priv-Doz. Dr.' THEN 'Priv.-Doz'
	WHEN ident.anti ='Priv.-Doz. Dr.           ' THEN 'Priv.-Doz'	
	WHEN ident.anti ='PD Dr. rer. nat. habil.  ' THEN 'Priv.-Doz'	
	WHEN ident.anti ='PD Dr.' THEN 'Priv.-Doz'
	WHEN ident.anti ='PD Dr.-Ing.habil.        ' THEN 'Priv.-Doz'
	WHEN ident.anti ='Gast-Prof. Dr.' THEN 'Gast-Prof.'
	WHEN ident.anti ='Jun.-Prof. Dr.' THEN 'Jun.-Prof.'
	WHEN ident.anti ='Prof. Dr. rer. nat.      ' THEN 'Prof.'
	WHEN ident.anti ='Prof.Dr.rer.nat.         ' THEN 'Prof.'
	WHEN ident.anti ='Prof.Dr. rer. nat. habil.' THEN 'Prof.'
	WHEN ident.anti ='Prof.Dr.rer.nat.habil.   ' THEN 'Prof.'
	
	
	ELSE ident.anti 
END as title_ohne_academic_degree 
FROM k_ppruef 
     		INNER JOIN identroll ON (identroll.verbindung_char = k_ppruef.ppruefer AND identroll.rolle = 'P')
            INNER  JOIN ident ON (ident.identnr = identroll.identnr)
            LEFT OUTER JOIN identroll AS identroll2 ON (identroll.identnr = identroll2.identnr AND identroll2.rolle IN ('F')) 
            LEFT JOIN identzusatz ON (ident.identnr=identzusatz.identnr AND identzusatz.spaltenname='SVA')
            WHERE ident.name IS NOT NULL AND TRIM(ident.name) != '' 
            AND (k_ppruef.prgrup NOT IN ('O','M','U') OR k_ppruef.prgrup IS NULL)
               -- [pruefer_restriction]
			 --AND ident.name = 'Rogler'
             --ORDER BY ident.identnr
			 AND ident.anti is distinct from null



select * from k_ppruef
select * from parstg