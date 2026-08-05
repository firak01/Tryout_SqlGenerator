select abtitelhm, abtitelhw, * from parstg
where abschl in ('11')
and stg in ('032')
and pversion in (2015)
order by psem DESC

--############################################
select distinct abschl, stg, abtitelhm, abtitelhw from parstg