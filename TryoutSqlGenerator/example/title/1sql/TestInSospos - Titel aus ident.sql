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


--############################################

select distinct anti from ident

select distinct ident.anti as title_academicdegree 
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

