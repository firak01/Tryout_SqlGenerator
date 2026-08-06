set search_path='hisinone';
select * from degree 
LIMIT 100;

--#####################
select academicdegree_id, * from course_of_study
where academicdegree_id is distinct from null;

--######################
set search_path='hisinone';
select * from academicdegree;
select uniquename, * from course_of_study limit 100;

set search_path='hisinone';
select * from academicdegree;

select uniquename, * from course_of_study --limit 100;
where uniquename LIKE '11|032|-|-|H|%|0390|P|V|%|'
limit 100;


--######################
--### Entwickle das Update SQL
select id from academicdegree where uniquename in ('diplxing')

update course_of_study set academicdegree_id = (select id from academicdegree where uniquename in ('diplxing'))
where uniquename LIKE '11|032|-|-|H|%|0390|P|V|%|'

--
UPDATE course_of_study SET academicdegree_id = (SELECT id FROM academicdegree WHERE uniquename IN ( 'diplxmat' ))  WHERE uniquename IN ( '  |   |%|-|H|%|0390|P|V|%|' );



