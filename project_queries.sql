SET SQL_SAFE_UPDATES = 0;
delete from metrotransactions;
delete from customer;
delete from product;
delete from store;
delete from supplier;
delete from time;

/* QUERY 1 */
select sum(mt.sales), s.store_name
from metrotransactions AS mt
left join store AS s
on s.store_id = mt.store_id
left join time AS t
on t.time_id = mt.time_ID
where t.month = 9 and t.year = 2017
group by s.store_name
order by sum(mt.sales) desc limit 3;

/* QUERY 2 */
select sum(mt.sales), s.supplier_name
from metrotransactions AS mt
left join supplier AS s
on s.supplier_id = mt.supplier_id
left join time AS t
on t.time_id = mt.time_ID
where t.day = 'Saturday' or t.day = 'Sunday'
group by s.supplier_name
order by sum(mt.sales) desc limit 10;

/* QUERY 3 */
select s.supplier_name, t.month, t.quarter, sum(mt.sales)
from metrotransactions AS mt
right join product AS p
on p.product_id = mt.product_id
right join supplier AS s
on s.supplier_id = mt.supplier_id
right join time AS t
on t.time_id = mt.time_id
group by t.month, t.quarter
order by t.month, t.quarter;

/* QUERY 4*/
select p.product_name, s.store_name, sum(mt.sales)
from metrotransactions AS mt
right join product AS p
on p.product_id = mt.product_id
right join store AS s
on s.store_id = mt.store_id
group by s.store_name, p.product_name
order by s.STORE_NAME;

/* QUERY 5*/
select s.store_name, sum(mt.sales), t.quarter
from metrotransactions as mt
right join store as s
on s.store_id = mt.store_id
right join time AS t
on t.time_id = mt.time_id
group by s.store_name, t.quarter;


/* QUERY 6*/
select sum(mt.quantity), p.product_name
from metrotransactions AS mt
right join product AS p
on p.product_id = mt.product_id
right join time AS t
on t.time_id = mt.time_id
where t.day = 'Saturday' or t.day = 'Sunday'
group by p.product_name
order by sum(mt.quantity) desc limit 5;

/* QUERY 7*/
select mt.store_id, mt.supplier_id, mt.product_id,
avg(mt.sales) AS 'Avg Sales', max(mt.sales) AS 'Max Sales', min(mt.sales) AS 'Min Sales'
from metrotransactions AS mt
group by mt.store_id, mt.supplier_id, mt.product_id
with rollup;

/* QUERY 8*/
select p.product_name, sum(mt.sales), 
if(ceiling(t.month/6) = 1, "First", "Second") AS Half 
from metrotransactions as mt
right join product as p 
on p.product_id = mt.product_id
right join time AS t
on t.time_id = mt.time_id
where t.year = 2017
group by p.product_name,
ceiling(t.month/6);

select p.product_name, t.year, sum(mt.sales) 
from metrotransactions as mt
right join product as p 
on p.product_id = mt.product_id
right join time AS t
on t.time_id = mt.time_id
where t.year = 2017
group by p.product_name, t.year;

/* QUERY 9 */
select mt.sales, mt.sale_id 
from metrotransactions as mt
where mt.sales not between
(select avg(mt.sales)-3*stddev(mt.sales) 
from metrotransactions as mt) 
and
(select avg(mt.sales)+3*stddev(mt.sales) 
from metrotransactions as mt);

/* QUERY 10 */
drop materialized view store_product_analysis;
create materialized view store_product_analysis build immediate
refresh complete
AS
	select s.store_name, p.product_name, sum(mt.sales)
    from metrotransactions AS mt
    right join store as s
	on s.store_id = mt.store_id
    left join product AS p
	on p.product_id = mt.product_id
    group by s.store_name, p.product_name
    order by s.store_name, p.product_name;









