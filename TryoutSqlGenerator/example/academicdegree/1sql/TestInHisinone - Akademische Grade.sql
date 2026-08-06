set search_path='hisinone';
select * from degree 
LIMIT 100;

--#####################
select academicdegree_id, * from course_of_study
where academicdegree_id is distinct from null;

--######################
select * from academicdegree;
